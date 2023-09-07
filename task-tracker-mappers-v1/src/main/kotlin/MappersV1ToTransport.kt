package ru.otus.otuskotlin.tasktracker.mappers.v1

import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.MkplContext
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions.UnknownMkplCommand

fun MkplContext.toTransportTask(): IResponse = when (val cmd = command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportUpdate()
    MkplCommand.DELETE -> toTransportDelete()
    MkplCommand.SEARCH -> toTransportSearch()
    MkplCommand.NONE -> throw UnknownMkplCommand(cmd)
}

fun MkplContext.toTransportCreate() = TaskCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MkplContext.toTransportRead() = TaskReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MkplContext.toTransportUpdate() = TaskUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MkplContext.toTransportDelete() = TaskDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MkplContext.toTransportSearch() = TaskSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    tasks = tasksResponse.toTransportAd()
)

fun List<MkplTask>.toTransportAd(): List<TaskResponseObject>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun MkplContext.toTransportInit() = TaskInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun MkplTask.toTransportTask(): TaskResponseObject = TaskResponseObject(
    id = id.takeIf { it != MkplTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    priority = priority.toTransportTask(),
    status = status.toTransportTask(),
    permissions = permissionsClient.toTransportTask(),
)

private fun Set<MkplTaskPermissionClient>.toTransportTask(): Set<TaskPermissions>? = this
    .map { it.toTransportTask() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplTaskPermissionClient.toTransportTask() = when (this) {
    MkplTaskPermissionClient.READ -> TaskPermissions.READ
    MkplTaskPermissionClient.UPDATE -> TaskPermissions.UPDATE
    MkplTaskPermissionClient.DELETE -> TaskPermissions.DELETE
}

private fun MkplPriority.toTransportTask() = when (this) {
    MkplPriority.LOW -> TaskPriority.LOW
    MkplPriority.HIGH -> TaskPriority.HIGH
    MkplPriority.CRITICAL -> TaskPriority.CRITICAL
    MkplPriority.NONE -> null
}

private fun MkplStatus.toTransportTask() = when (this) {
    MkplStatus.TO_DO -> TaskStatus.TO_DO
    MkplStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    MkplStatus.DONE -> TaskStatus.DONE
    MkplStatus.DELETED -> TaskStatus.DELETED
    MkplStatus.NONE -> null
}

private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportTask() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
