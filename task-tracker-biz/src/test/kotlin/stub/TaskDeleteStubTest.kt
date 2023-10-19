package ru.otus.otuskotlin.tasktracker.biz.stub

import TaskStub
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskDeleteStubTest {

    private val processor = TaskProcessor()
    val id = TaskId("666")

    @Test
    fun delete() = runTest {

        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = TaskStub.get()
        assertEquals(stub.id, ctx.taskResponse.id)
        assertEquals(stub.title, ctx.taskResponse.title)
        assertEquals(stub.description, ctx.taskResponse.description)
        assertEquals(stub.priority, ctx.taskResponse.priority)
        assertEquals(stub.status, ctx.taskResponse.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            taskRequest = Task(),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TITLE,
            taskRequest = Task(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
