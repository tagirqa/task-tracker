package ru.otus.otuskotlin.tasktracker.mappers.v1

import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.api.v1.models.Error as ApiModelsError
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions.UnknownMkplCommand

fun Context.toTransportTask(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.NONE -> throw UnknownMkplCommand(cmd)
}

fun Context.toTransportCreate() = TaskCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = appErrors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun Context.toTransportRead() = TaskReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = appErrors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun Context.toTransportUpdate() = TaskUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = appErrors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun Context.toTransportDelete() = TaskDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = appErrors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun Context.toTransportSearch() = TaskSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = appErrors.toTransportErrors(),
    tasks = tasksResponse.toTransportAd()
)

fun List<Task>.toTransportAd(): List<TaskResponseObject>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun Context.toTransportInit() = TaskInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (appErrors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = appErrors.toTransportErrors(),
)

private fun Task.toTransportTask(): TaskResponseObject = TaskResponseObject(
    id = id.takeIf { it != TaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    priority = priority.toTransportTask(),
    status = status.toTransportTask(),
    lock = lock.takeIf { it != TaskLock.NONE }?.asString(),
    permissions = permissionsClient.toTransportTask(),
)

private fun Set<TaskPermissionClient>.toTransportTask(): Set<TaskPermissions>? = this
    .map { it.toTransportTask() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun TaskPermissionClient.toTransportTask() = when (this) {
    TaskPermissionClient.READ -> TaskPermissions.READ
    TaskPermissionClient.UPDATE -> TaskPermissions.UPDATE
    TaskPermissionClient.DELETE -> TaskPermissions.DELETE
}

private fun Priority.toTransportTask() = when (this) {
    Priority.LOW -> TaskPriority.LOW
    Priority.HIGH -> TaskPriority.HIGH
    Priority.CRITICAL -> TaskPriority.CRITICAL
    Priority.NONE -> null
}

private fun Status.toTransportTask() = when (this) {
    Status.TO_DO -> TaskStatus.TO_DO
    Status.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    Status.DONE -> TaskStatus.DONE
    Status.DELETED -> TaskStatus.DELETED
    Status.NONE -> null
}

private fun List<AppError>.toTransportErrors(): List<ApiModelsError>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AppError.toTransportTask() = ApiModelsError(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    State.NONE -> null
}