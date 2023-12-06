package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model

import ru.otus.otuskotlin.tasktracker.common.models.*

data class TaskEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val priority: String? = null,
    val status: String? = null,
    val lock: String? = null,
) {
    constructor(model: Task): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        priority = model.priority.takeIf { it != Priority.NONE }?.name,
        status = model.status.takeIf { it != Status.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Task(
        id = id?.let { TaskId(it) }?: TaskId.NONE,
        title = title?: "",
        description = description?: "",
        ownerId = ownerId?.let { UserId(it) }?: UserId.NONE,
        priority = priority?.let { Priority.valueOf(it) }?: Priority.NONE,
        status = status?.let { Status.valueOf(it) }?: Status.NONE,
        lock = lock?.let { TaskLock(it) } ?: TaskLock.NONE,
    )
}
