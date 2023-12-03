package ru.otus.otuskotlin.tasktracker.biz.stub

import ru.otus.otuskotlin.tasktracker.stub.TaskStub
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskReadStubTest {

    private val processor = TaskProcessor()
    val id = TaskId("666")

    @Test
    fun read() = runTest {

        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (TaskStub.get()) {
            assertEquals(id, ctx.taskResponse.id)
            assertEquals(title, ctx.taskResponse.title)
            assertEquals(description, ctx.taskResponse.description)
            assertEquals(priority, ctx.taskResponse.priority)
            assertEquals(status, ctx.taskResponse.status)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            taskRequest = Task(),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("id", ctx.appErrors.firstOrNull()?.field)
        assertEquals("validation", ctx.appErrors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("internal", ctx.appErrors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TITLE,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("stub", ctx.appErrors.firstOrNull()?.field)
    }
}
