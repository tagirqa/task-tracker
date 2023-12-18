package ru.otus.otuskotlin.tasktracker.mappers.v1

import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = TaskUpdateRequest(
            requestId = "1234",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = TaskUpdateObject(
                id = "12345",
                title = "title",
                description = "desc",
                priority = TaskPriority.CRITICAL,
                status = TaskStatus.IN_PROGRESS,
                lock = "456789",
            ),
        )

        val context = Context()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("12345", context.taskRequest.id.asString())
        assertEquals("456789", context.taskRequest.lock.asString())
        assertEquals("title", context.taskRequest.title)
        assertEquals(Priority.CRITICAL, context.taskRequest.priority)
        assertEquals(Status.IN_PROGRESS, context.taskRequest.status)
    }

    @Test
    fun toTransport() {
        val context = Context(
            requestId = RequestId("1234"),
            command = Command.UPDATE,
            taskResponse = Task(
                id = TaskId("12345"),
                title = "title",
                description = "desc",
                priority = Priority.CRITICAL,
                status = Status.IN_PROGRESS,
                lock = TaskLock("456789"),
            ),
            appErrors = mutableListOf(
                AppError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = State.RUNNING,
        )

        val req = context.toTransportTask() as TaskUpdateResponse

        assertEquals("1234", req.requestId)
        assertEquals("12345", req.task?.id)
        assertEquals("456789", req.task?.lock)
        assertEquals("title", req.task?.title)
        assertEquals("desc", req.task?.description)
        assertEquals(TaskPriority.CRITICAL, req.task?.priority)
        assertEquals(TaskStatus.IN_PROGRESS, req.task?.status)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
