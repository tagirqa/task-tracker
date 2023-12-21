package ru.otus.otuskotlin.tasktracker.auth

import ru.otus.otuskotlin.tasktracker.common.models.Command
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalRelations
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerUserPermissions


fun checkPermitted(
    command: Command,
    relations: Iterable<TaskTrackerPrincipalRelations>,
    permissions: Iterable<TaskTrackerUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: Command,
    val permission: TaskTrackerUserPermissions,
    val relation: TaskTrackerPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = Command.CREATE,
        permission = TaskTrackerUserPermissions.CREATE_OWN,
        relation = TaskTrackerPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = Command.READ,
        permission = TaskTrackerUserPermissions.READ_OWN,
        relation = TaskTrackerPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = Command.READ,
        permission = TaskTrackerUserPermissions.READ_PUBLIC,
        relation = TaskTrackerPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = Command.UPDATE,
        permission = TaskTrackerUserPermissions.UPDATE_OWN,
        relation = TaskTrackerPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = Command.DELETE,
        permission = TaskTrackerUserPermissions.DELETE_OWN,
        relation = TaskTrackerPrincipalRelations.OWN,
    ) to true,

)
