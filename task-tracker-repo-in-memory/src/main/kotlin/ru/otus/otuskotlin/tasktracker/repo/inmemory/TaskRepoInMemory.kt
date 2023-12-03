package ru.otus.otuskotlin.tasktracker.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model.TaskEntity
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class TaskRepoInMemory(
    initObjects: Collection<Task> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = {
        uuid4().toString()
    }
) : ITaskRepository {

    private val cache = Cache.Builder<String, TaskEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: Task) {
        val entity = TaskEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        val key = randomUuid()
        val task = rq.task.copy(id = TaskId(key), lock = TaskLock(randomUuid()))
        val entity = TaskEntity(task)
        cache.put(key, entity)
        return DbTaskResponse(
            data = task,
            isSuccess = true,
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
        val key = rq.id.takeIf {
            it != TaskId.NONE
        }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)?.let {
            DbTaskResponse(
                data = it.toInternal(),
                isSuccess = true
            )
        } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(
        id: TaskId,
        oldLock: TaskLock,
        okBlock: (key: String, oldTask: TaskEntity) -> DbTaskResponse
    ): DbTaskResponse {
        val key = id.takeIf { it != TaskId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockStr = oldLock.takeIf { it != TaskLock.NONE }?.asString()
            ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldTask = cache.get(key)
            when {
                oldTask == null -> resultErrorNotFound
                oldTask.lock != oldLockStr -> DbTaskResponse.errorConcurrent(
                    oldLock,
                    oldTask.toInternal()
                )

                else -> okBlock(key, oldTask)
            }
        }
    }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse =
        doUpdate(rq.task.id, rq.task.lock) { key, _ ->
            val newTask = rq.task.copy(lock = TaskLock(randomUuid()))
            val entity = TaskEntity(newTask)
            cache.put(key, entity)
            DbTaskResponse.success(newTask)
        }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse =
        doUpdate(rq.id, rq.lock) { key, oldTask ->
            cache.invalidate(key)
            DbTaskResponse.success(oldTask.toInternal())
        }

    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != UserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.priority.takeIf { it != Priority.NONE }?.let {
                    it.name == entry.value.priority
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != Status.NONE }?.let {
                    it.name == entry.value.status
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbTasksResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbTaskResponse(
            data = null,
            isSuccess = false,
            appErrors = listOf(
                AppError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbTaskResponse(
            data = null,
            isSuccess = false,
            appErrors = listOf(
                AppError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbTaskResponse(
            isSuccess = false,
            data = null,
            appErrors = listOf(
                AppError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}