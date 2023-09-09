package ru.otus.otuskotlin.tasktracker.common.models

data class Task(
    var id: TaskId = TaskId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: UserId = UserId.NONE,
    var priority: Priority = Priority.NONE,
    var status: Status = Status.NONE,
    var productId: ProductId = ProductId.NONE,
    val permissionsClient: MutableSet<TaskPermissionClient> = mutableSetOf()
)
