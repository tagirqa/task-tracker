package ru.otus.otuskotlin.tasktracker.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskUpdateStubTest {

    private val processor = TaskProcessor()
    val id = TaskId("777")
    val title = "title 666"
    val description = "desc 666"
    val priority = Priority.HIGH
    val status = Status.TO_DO

    @Test
    fun create() = runTest {

        val ctx = Context(
            command = Command.UPDATE,
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
        assertEquals(id, ctx.taskResponse.id)
        assertEquals(title, ctx.taskResponse.title)
        assertEquals(description, ctx.taskResponse.description)
        assertEquals(priority, ctx.taskResponse.priority)
        assertEquals(status, ctx.taskResponse.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
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
            command = Command.UPDATE,
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
            command = Command.UPDATE,
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
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SEARCH_STRING,
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
