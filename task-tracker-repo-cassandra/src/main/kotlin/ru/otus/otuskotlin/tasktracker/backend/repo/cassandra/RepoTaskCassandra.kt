package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.TaskCassandraDTO
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.TaskPriority
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.TaskStatus
import ru.otus.otuskotlin.tasktracker.common.helpers.asTaskTrackerError
import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskId
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock
import ru.otus.otuskotlin.tasktracker.common.repo.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepoTaskCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<Task> = emptyList(),
) : ITaskRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(TaskPriority::class.java))
            register(EnumNameCodec(TaskStatus::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(TaskCassandraDTO.table(keyspace, TaskCassandraDTO.TABLE_NAME))
        session.execute(TaskCassandraDTO.titleIndex(keyspace, TaskCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.taskDao(keyspaceName, TaskCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(TaskCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToAdResponse(e: Exception) = DbTaskResponse.error(e.asTaskTrackerError())
    private fun errorToAdsResponse(e: Exception) = DbTasksResponse.error(e.asTaskTrackerError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        val new = rq.task.copy(id = TaskId(randomUuid()), lock = TaskLock(randomUuid()))
        return doDbAction(
            "create",
            { dao.create(TaskCassandraDTO(new)) },
            { DbTaskResponse.success(new) },
            ::errorToAdResponse
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse =
        if (rq.id == TaskId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            "read",
            { dao.read(rq.id.asString()) },
            { found ->
                if (found != null) DbTaskResponse.success(found.toTaskModel())
                else ID_NOT_FOUND
            },
            ::errorToAdResponse
        )

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
        val idStr = rq.task.id.asString()
        val prevLock = rq.task.lock.asString()
        val new = rq.task.copy(lock = TaskLock(randomUuid()))
        val dto = TaskCassandraDTO(new)

        return doDbAction(
            "update",
            {
                val res = dao.update(dto, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(TaskCassandraDTO.COLUMN_LOCK) }
                    ?.getString(TaskCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    // Два варианта почти эквивалентны, выбирайте который вам больше подходит
                    isSuccess -> DbTaskResponse.success(new)
                    // res.wasApplied() -> DbAdResponse.success(dao.read(idStr).await()?.toAdModel())
                    resultField == null -> DbTaskResponse(null, false, ID_NOT_FOUND.appErrors)
                    else -> DbTaskResponse(
                        dao.read(idStr).await()?.toTaskModel(),
                        false,
                        CONCURRENT_MODIFICATION.appErrors
                    )
                }
            },
            ::errorToAdResponse
        )
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
        return doDbAction(
            "delete",
            {
                val idStr = rq.id.asString()
                val prevLock = rq.lock.asString()
                val oldAd = dao.read(idStr).await()?.toTaskModel() ?: return@doDbAction ID_NOT_FOUND
                val res = dao.delete(idStr, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(TaskCassandraDTO.COLUMN_LOCK) }
                    ?.getString(TaskCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    // Два варианта почти эквивалентны, выбирайте который вам больше подходит
                    isSuccess -> DbTaskResponse.success(oldAd)
                    resultField == null -> DbTaskResponse(null, false, ID_NOT_FOUND.appErrors)
                    else -> DbTaskResponse(
                        dao.read(idStr).await()?.toTaskModel(),
                        false,
                        CONCURRENT_MODIFICATION.appErrors
                    )
                }
            },
            ::errorToAdResponse
        )
    }


    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse =
        doDbAction(
            "search",
            { dao.search(rq) },
            { found ->
                DbTasksResponse.success(found.map { it.toTaskModel() })
            },
            ::errorToAdsResponse
        )

    companion object {
        private val ID_IS_EMPTY = DbTaskResponse.error(AppError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            DbTaskResponse.error(AppError(field = "id", code = "not-found", message = "Not Found"))
        private val CONCURRENT_MODIFICATION =
            DbTaskResponse.error(AppError(field = "lock", code = "concurrency", message = "Concurrent modification"))
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }