package ru.otus.otuskotlin.tasktracker.common.permissions

import ru.otus.otuskotlin.tasktracker.common.models.UserId

data class TaskTrackerPrincipalModel(
    val id: UserId = UserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<TaskTrackerUserGroups> = emptySet()
) {
    companion object {
        val NONE = TaskTrackerPrincipalModel()
    }
}
