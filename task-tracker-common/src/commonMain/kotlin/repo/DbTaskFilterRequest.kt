package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.models.Priority
import ru.otus.otuskotlin.tasktracker.common.models.Status
import ru.otus.otuskotlin.tasktracker.common.models.UserId

data class DbTaskFilterRequest(
    val titleFilter: String = "",
    val ownerId: UserId = UserId.NONE,
    val status: Status = Status.NONE,
    val priority: Priority = Priority.NONE
)
