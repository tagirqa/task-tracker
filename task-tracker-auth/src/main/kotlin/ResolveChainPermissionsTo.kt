package ru.otus.otuskotlin.tasktracker.auth

import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerUserGroups
import ru.otus.otuskotlin.tasktracker.common.permissions.TaskTrackerUserPermissions

fun resolveChainPermissions(
    groups: Iterable<TaskTrackerUserGroups>,
) = mutableSetOf<TaskTrackerUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    TaskTrackerUserGroups.USER to setOf(
        TaskTrackerUserPermissions.READ_OWN,
        TaskTrackerUserPermissions.READ_PUBLIC,
        TaskTrackerUserPermissions.CREATE_OWN,
        TaskTrackerUserPermissions.UPDATE_OWN,
        TaskTrackerUserPermissions.DELETE_OWN,
        TaskTrackerUserPermissions.OFFER_FOR_OWN,
    ),
    TaskTrackerUserGroups.MODERATOR_MP to setOf(),
    TaskTrackerUserGroups.ADMIN_AD to setOf(),
    TaskTrackerUserGroups.TEST to setOf(),
    TaskTrackerUserGroups.BAN_AD to setOf(),
)

private val groupPermissionsDenys = mapOf(
    TaskTrackerUserGroups.USER to setOf(),
    TaskTrackerUserGroups.MODERATOR_MP to setOf(),
    TaskTrackerUserGroups.ADMIN_AD to setOf(),
    TaskTrackerUserGroups.TEST to setOf(),
    TaskTrackerUserGroups.BAN_AD to setOf(
        TaskTrackerUserPermissions.UPDATE_OWN,
        TaskTrackerUserPermissions.CREATE_OWN,
    ),
)
