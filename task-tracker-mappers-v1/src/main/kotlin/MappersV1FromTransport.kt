package ru.otus.otuskotlin.tasktracker.mappers.v1

import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions.UnknownRequestClass

fun Context.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTaskId() = this?.let { TaskId(it) } ?: TaskId.NONE
private fun String?.toTaskWithId() = Task(id = this.toTaskId())
private fun IRequest?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE
private fun String?.toTaskLock() = this?.let { TaskLock(it) } ?: TaskLock.NONE

private fun TaskDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    TaskRequestDebugMode.PROD -> WorkMode.PROD
    TaskRequestDebugMode.TEST -> WorkMode.TEST
    TaskRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun TaskDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    TaskRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    TaskRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    TaskRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    TaskRequestDebugStubs.BAD_TITLE -> Stubs.BAD_TITLE
    TaskRequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    TaskRequestDebugStubs.BAD_PRIORITY -> Stubs.BAD_PRIORITY
    TaskRequestDebugStubs.BAD_STATUS -> Stubs.BAD_STATUS
    TaskRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    TaskRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun Context.fromTransport(request: TaskCreateRequest) {
    command = Command.CREATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: Task()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: TaskReadRequest) {
    command = Command.READ
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: TaskUpdateRequest) {
    command = Command.UPDATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: Task()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: TaskDeleteRequest) {
    command = Command.DELETE
    requestId = request.requestId()
    taskRequest = request.task.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskDeleteObject?.toInternal(): Task = if (this != null) {
    Task(
        id = id.toTaskId(),
        lock = lock.toTaskLock(),
    )
} else {
    Task()
}

fun Context.fromTransport(request: TaskSearchRequest) {
    command = Command.SEARCH
    requestId = request.requestId()
    taskFilterRequest = request.taskFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskSearchFilter?.toInternal(): TaskFilter = TaskFilter(
    title = this?.title ?: "",
    priority = this?.priority.fromTransport(),
    status = this?.status.fromTransport()
)

private fun TaskCreateObject.toInternal(): Task = Task(
    title = this.title ?: "",
    description = this.description ?: "",
    priority = this.priority.fromTransport(),
    status = this.status.fromTransport(),
)

private fun TaskUpdateObject.toInternal(): Task = Task(
    id = this.id.toTaskId(),
    title = this.title ?: "",
    description = this.description ?: "",
    priority = this.priority.fromTransport(),
    status = this.status.fromTransport(),
    lock = lock.toTaskLock()
)

private fun TaskPriority?.fromTransport(): Priority = when (this) {
    TaskPriority.LOW -> Priority.LOW
    TaskPriority.HIGH -> Priority.HIGH
    TaskPriority.CRITICAL -> Priority.CRITICAL
    null -> Priority.NONE
}

private fun TaskStatus?.fromTransport(): Status = when (this) {
    TaskStatus.TO_DO -> Status.TO_DO
    TaskStatus.IN_PROGRESS -> Status.IN_PROGRESS
    TaskStatus.DONE -> Status.DONE
    TaskStatus.DELETED -> Status.DELETED
    null -> Status.NONE
}