package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import ru.otus.otuskotlin.tasktracker.common.models.*

@Entity
data class TaskCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: String? = null,
    @field:CqlName(COLUMN_TITLE)
    var title: String? = null,
    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_STATUS)
    var status: TaskStatus? = null,
    @field:CqlName(COLUMN_PRIORITY)
    var priority: TaskPriority? = null,
    @field:CqlName(COLUMN_PRODUCT)
    var productId: String? = null,
    // Нельзя использовать в моделях хранения внутренние модели.
    // При изменении внутренних моделей, БД автоматически не изменится,
    // а потому будет Runtime ошибка, которая вылезет только на продуктовом стенде
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(taskModel: Task) : this(
        ownerId = taskModel.ownerId.takeIf { it != UserId.NONE }?.asString(),
        id = taskModel.id.takeIf { it != TaskId.NONE }?.asString(),
        title = taskModel.title.takeIf { it.isNotBlank() },
        description = taskModel.description.takeIf { it.isNotBlank() },
        priority = taskModel.priority.toTransport(),
        productId = taskModel.productId.takeIf { it != ProductId.NONE }?.asString(),
        status = taskModel.status.toTransport(),
        lock = taskModel.lock.takeIf { it != TaskLock.NONE }?.asString()
    )

    fun toTaskModel(): Task =
        Task(
            ownerId = ownerId?.let { UserId(it) } ?: UserId.NONE,
            id = id?.let { TaskId(it) } ?: TaskId.NONE,
            title = title ?: "",
            description = description ?: "",
            priority = priority.fromTransport(),
            productId = productId?.let { ProductId(it) } ?: ProductId.NONE,
            status = status.fromTransport(),
            lock = lock?.let { TaskLock(it) } ?: TaskLock.NONE
        )

    companion object {
        const val TABLE_NAME = "tasks"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_OWNER_ID = "owner_id_my"
        const val COLUMN_PRODUCT = "product"
        const val COLUMN_LOCK = "lock"
        const val COLUMN_STATUS = "status"
        const val COLUMN_PRIORITY = "priority"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_TITLE, DataTypes.TEXT)
                .withColumn(COLUMN_DESCRIPTION, DataTypes.TEXT)
                .withColumn(COLUMN_OWNER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_STATUS, DataTypes.TEXT)
                .withColumn(COLUMN_PRODUCT, DataTypes.TEXT)
                .withColumn(COLUMN_PRIORITY, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

        fun titleIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_TITLE)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}
