package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.TaskCassandraDTO
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model.toTransport
import ru.otus.otuskotlin.tasktracker.common.models.Priority
import ru.otus.otuskotlin.tasktracker.common.models.Status
import ru.otus.otuskotlin.tasktracker.common.models.UserId
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class TaskCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<TaskCassandraDTO>
) {
    fun search(filter: DbTaskFilterRequest): CompletionStage<Collection<TaskCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.titleFilter.isNotBlank()) {
            select = select
                .whereColumn(TaskCassandraDTO.COLUMN_TITLE)
                .like(QueryBuilder.literal("%${filter.titleFilter}%"))
        }
        if (filter.ownerId != UserId.NONE) {
            select = select
                .whereColumn(TaskCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.priority != Priority.NONE) {
            select = select
                .whereColumn(TaskCassandraDTO.COLUMN_PRIORITY)
                .isEqualTo(QueryBuilder.literal(filter.priority.toTransport(), context.session.context.codecRegistry))
        }

        if (filter.status != Status.NONE) {
            select = select
                .whereColumn(TaskCassandraDTO.COLUMN_STATUS)
                .isEqualTo(QueryBuilder.literal(filter.status.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<TaskCassandraDTO>()
        private val future = CompletableFuture<Collection<TaskCassandraDTO>>()
        val stage: CompletionStage<Collection<TaskCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}