package ru.otus.otuskotlin.tasktracker.auth

import ru.otus.otuskotlin.tasktracker.common.models.TaskPermissionClient
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerPrincipalRelations
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<TaskTrackerUserPermissions>,
    relations: Iterable<TaskTrackerPrincipalRelations>,
) = mutableSetOf<TaskPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    TaskTrackerUserPermissions.READ_OWN to mapOf(
        TaskTrackerPrincipalRelations.OWN to TaskPermissionClient.READ
    ),
    TaskTrackerUserPermissions.READ_GROUP to mapOf(
        TaskTrackerPrincipalRelations.GROUP to TaskPermissionClient.READ
    ),
    TaskTrackerUserPermissions.READ_PUBLIC to mapOf(
        TaskTrackerPrincipalRelations.PUBLIC to TaskPermissionClient.READ
    ),
    TaskTrackerUserPermissions.READ_CANDIDATE to mapOf(
        TaskTrackerPrincipalRelations.MODERATABLE to TaskPermissionClient.READ
    ),

    // UPDATE
    TaskTrackerUserPermissions.UPDATE_OWN to mapOf(
        TaskTrackerPrincipalRelations.OWN to TaskPermissionClient.UPDATE
    ),
    TaskTrackerUserPermissions.UPDATE_PUBLIC to mapOf(
        TaskTrackerPrincipalRelations.MODERATABLE to TaskPermissionClient.UPDATE
    ),
    TaskTrackerUserPermissions.UPDATE_CANDIDATE to mapOf(
        TaskTrackerPrincipalRelations.MODERATABLE to TaskPermissionClient.UPDATE
    ),

    // DELETE
    TaskTrackerUserPermissions.DELETE_OWN to mapOf(
        TaskTrackerPrincipalRelations.OWN to TaskPermissionClient.DELETE
    ),
    TaskTrackerUserPermissions.DELETE_PUBLIC to mapOf(
        TaskTrackerPrincipalRelations.MODERATABLE to TaskPermissionClient.DELETE
    ),
    TaskTrackerUserPermissions.DELETE_CANDIDATE to mapOf(
        TaskTrackerPrincipalRelations.MODERATABLE to TaskPermissionClient.DELETE
    ),
)
