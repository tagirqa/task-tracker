package ru.otus.otuskotlin.tasktracker.common.models

import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalRelations

data class Task(
    var id: TaskId = TaskId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: UserId = UserId.NONE,
    var priority: Priority = Priority.NONE,
    var status: Status = Status.NONE,
    var productId: ProductId = ProductId.NONE,
    val permissionsClient: MutableSet<TaskPermissionClient> = mutableSetOf(),
    var lock: TaskLock = TaskLock.NONE,
    var principalRelations: Set<TaskTrackerPrincipalRelations> = emptySet(),
) {
    fun deepCopy(): Task = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

}
