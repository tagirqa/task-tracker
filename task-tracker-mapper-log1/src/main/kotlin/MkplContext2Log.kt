package ru.otus.otuskotlin.tasktracker.api.logs.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.tasktracker.api.logs.models.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*

fun Context.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "task-tracker",
    task = toTaskLog(),
    errors = appErrors.map { it.toLog() },
)

fun Context.toTaskLog(): MkplLogModel? {
    val adNone = Task()
    return MkplLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        operation = command.toLogModel(),
        requestTask = taskRequest.takeIf { it != adNone }?.toLog(),
        responseTask = taskResponse.takeIf { it != adNone }?.toLog(),
        responseTasks = tasksResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = taskFilterRequest.takeIf { it != TaskFilter() }?.toLog(),
    ).takeIf { it != MkplLogModel() }
}

private fun Command.toLogModel(): MkplLogModel.Operation? = when(this) {
    Command.CREATE -> MkplLogModel.Operation.CREATE
    Command.READ -> MkplLogModel.Operation.READ
    Command.UPDATE -> MkplLogModel.Operation.UPDATE
    Command.DELETE -> MkplLogModel.Operation.DELETE
    Command.SEARCH -> MkplLogModel.Operation.SEARCH
    Command.NONE -> null
}

private fun TaskFilter.toLog() = TaskFilterLog(
    title = title.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    priority = priority.takeIf { it != Priority.NONE }?.name,
    status = status.takeIf { it != Status.NONE }?.name,
)

fun AppError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun Task.toLog() = TaskLog(
    id = id.takeIf { it != TaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    priority = priority.takeIf { it != Priority.NONE }?.name,
    status = status.takeIf { it != Status.NONE }?.name,
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    productId = productId.takeIf { it != ProductId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
