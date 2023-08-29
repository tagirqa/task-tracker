package ru.otus.otuskotlin.tasktracker.mappers.v1

import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.MkplContext
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.MkplStubs
import ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions.UnknownRequestClass

fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTaskId() = this?.let { MkplTaskId(it) } ?: MkplTaskId.NONE
private fun String?.toTaskWithId() = MkplTask(id = this.toTaskId())
private fun IRequest?.requestId() = this?.requestId?.let { MkplRequestId(it) } ?: MkplRequestId.NONE

private fun TaskDebug?.transportToWorkMode(): MkplWorkMode = when (this?.mode) {
    TaskRequestDebugMode.PROD -> MkplWorkMode.PROD
    TaskRequestDebugMode.TEST -> MkplWorkMode.TEST
    TaskRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun TaskDebug?.transportToStubCase(): MkplStubs = when (this?.stub) {
    TaskRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    TaskRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    TaskRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    TaskRequestDebugStubs.BAD_TITLE -> MkplStubs.BAD_TITLE
    TaskRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    TaskRequestDebugStubs.BAD_PRIORITY -> MkplStubs.BAD_PRIORITY
    TaskRequestDebugStubs.BAD_STATUS -> MkplStubs.BAD_STATUS
    TaskRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    TaskRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
}

fun MkplContext.fromTransport(request: TaskCreateRequest) {
    command = MkplCommand.CREATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: MkplTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: TaskReadRequest) {
    command = MkplCommand.READ
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: TaskUpdateRequest) {
    command = MkplCommand.UPDATE
    requestId = request.requestId()
    taskRequest = request.task?.toInternal() ?: MkplTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: TaskDeleteRequest) {
    command = MkplCommand.DELETE
    requestId = request.requestId()
    taskRequest = request.task?.id.toTaskWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: TaskSearchRequest) {
    command = MkplCommand.SEARCH
    requestId = request.requestId()
    taskFilterRequest = request.taskFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskSearchFilter?.toInternal(): MkplTaskFilter = MkplTaskFilter(
    searchString = this?.searchString ?: ""
)

private fun TaskCreateObject.toInternal(): MkplTask = MkplTask(
    title = this.title ?: "",
    description = this.description ?: "",
    priority = this.priority.fromTransport(),
    status = this.status.fromTransport(),
)

private fun TaskUpdateObject.toInternal(): MkplTask = MkplTask(
    id = this.id.toTaskId(),
    title = this.title ?: "",
    description = this.description ?: "",
    priority = this.priority.fromTransport(),
    status = this.status.fromTransport(),
)

private fun TaskPriority?.fromTransport(): MkplPriority = when (this) {
    TaskPriority.LOW -> MkplPriority.LOW
    TaskPriority.HIGH -> MkplPriority.HIGH
    TaskPriority.CRITICAL -> MkplPriority.CRITICAL
    null -> MkplPriority.NONE
}

private fun TaskStatus?.fromTransport(): MkplStatus = when (this) {
    TaskStatus.TO_DO -> MkplStatus.TO_DO
    TaskStatus.IN_PROGRESS -> MkplStatus.IN_PROGRESS
    TaskStatus.DONE -> MkplStatus.DONE
    TaskStatus.DELETED -> MkplStatus.DELETED
    null -> MkplStatus.NONE
}