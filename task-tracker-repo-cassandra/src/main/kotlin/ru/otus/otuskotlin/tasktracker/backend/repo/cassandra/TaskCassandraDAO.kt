package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.TaskCassandraDTO
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface TaskCassandraDAO {
    @Insert
    fun create(dto: TaskCassandraDTO): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<TaskCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: TaskCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock = :prevLock", entityClass = [TaskCassandraDTO::class])
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = TaskCassandraSearchProvider::class, entityHelpers = [TaskCassandraDTO::class])
    fun search(filter: DbTaskFilterRequest): CompletionStage<Collection<TaskCassandraDTO>>
}
