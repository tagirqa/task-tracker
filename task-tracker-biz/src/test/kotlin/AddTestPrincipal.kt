package ru.otus.otuskotlin.tasktracker.biz

import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.UserId
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalModel
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerUserGroups

fun Context.addTestPrincipal(userId: UserId = UserId("321")) {
    principal = TaskTrackerPrincipalModel(
        id = userId,
        groups = setOf(
            TaskTrackerUserGroups.USER,
            TaskTrackerUserGroups.TEST,
        )
    )
}
