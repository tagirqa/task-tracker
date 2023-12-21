package ru.otus.otuskotlin.tasktracker.auth

import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskId
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalModel
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalRelations

fun Task.resolveRelationsTo(principal: TaskTrackerPrincipalModel): Set<TaskTrackerPrincipalRelations> = setOfNotNull(
    TaskTrackerPrincipalRelations.NONE,
    TaskTrackerPrincipalRelations.NEW.takeIf { id == TaskId.NONE },
    TaskTrackerPrincipalRelations.OWN.takeIf { principal.id == ownerId },
)
