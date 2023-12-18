package ru.otus.otuskotlin.tasktracker.common.models

data class TaskFilter(
    var title: String = "",
    var ownerId: UserId = UserId.NONE,
    var priority: Priority = Priority.NONE,
    var status: Status = Status.NONE
)
