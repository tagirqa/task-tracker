package ru.otus.otuskotlin.tasktracker.biz.stub

import ru.otus.otuskotlin.tasktracker.stub.TaskStub
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskCreateStubTest {

    private val processor = TaskProcessor()
    val id = TaskId("666")
    val title = "title 666"
    val description = "desc 666"
    val priority = Priority.HIGH
    val status = Status.DONE

    @Test
    fun create() = runTest {

        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            taskRequest = Task(
                id = id,
                title = title,
                description = description,
                priority = priority,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(TaskStub.get().id, ctx.taskResponse.id)
        assertEquals(title, ctx.taskResponse.title)
        assertEquals(description, ctx.taskResponse.description)
        assertEquals(priority, ctx.taskResponse.priority)
        assertEquals(status, ctx.taskResponse.status)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TITLE,
            taskRequest = Task(
                id = id,
                title = "",
                description = description,
                priority = priority,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("title", ctx.appErrors.firstOrNull()?.field)
        assertEquals("validation", ctx.appErrors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_DESCRIPTION,
            taskRequest = Task(
                id = id,
                title = title,
                description = "",
                priority = priority,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("description", ctx.appErrors.firstOrNull()?.field)
        assertEquals("validation", ctx.appErrors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.CREATE,
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
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            taskRequest = Task(
                id = id,
                title = title,
                description = description,
                priority = priority,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(Task(), ctx.taskResponse)
        assertEquals("stub", ctx.appErrors.firstOrNull()?.field)
        assertEquals("validation", ctx.appErrors.firstOrNull()?.group)
    }
}
