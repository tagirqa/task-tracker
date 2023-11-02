package ru.otus.otuskotlin.tasktracker.biz.stub

import ru.otus.otuskotlin.tasktracker.stub.TaskStub
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TaskSearchStubTest {

    private val processor = TaskProcessor()
    val filter = TaskFilter(priority = TaskStub.get().priority, status = TaskStub.get().status,)

    @Test
    fun read() = runTest {

        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        println(ctx.tasksResponse)
        assertTrue(ctx.tasksResponse.size > 1)
        val first = ctx.tasksResponse.firstOrNull() ?: fail("Empty response list")
        with (TaskStub.get()) {
            assertEquals(priority, first.priority)
            assertEquals(status, first.status)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TITLE,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
