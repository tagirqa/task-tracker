package ru.otus.otuskotlin.tasktracker.mappers.v1

import org.junit.Test
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.MkplContext
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.MkplStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = TaskCreateRequest(
            requestId = "1234",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = TaskCreateObject(
                title = "title",
                description = "desc",
                owner = "task owner",
                priority = TaskPriority.CRITICAL,
                status = TaskStatus.IN_PROGRESS
            ),
        )

        val context = MkplContext()
        context.fromTransport(req)

        assertEquals(MkplStubs.SUCCESS, context.stubCase)
        assertEquals(MkplWorkMode.STUB, context.workMode)
        assertEquals("title", context.taskRequest.title)
        assertEquals(MkplPriority.CRITICAL, context.taskRequest.priority)
        assertEquals(MkplStatus.IN_PROGRESS, context.taskRequest.status)
    }

    @Test
    fun toTransport() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.CREATE,
            taskResponse = MkplTask(
                title = "title",
                description = "desc",
                priority = MkplPriority.HIGH,
                status = MkplStatus.DONE
            ),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkplState.RUNNING,
        )

        val req = context.toTransportTask() as TaskCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.task?.title)
        assertEquals("desc", req.task?.description)
        assertEquals(TaskPriority.HIGH, req.task?.priority)
        assertEquals(TaskStatus.DONE, req.task?.status)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
