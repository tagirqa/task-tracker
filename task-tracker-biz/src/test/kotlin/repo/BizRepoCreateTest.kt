package ru.otus.otuskotlin.tasktracker.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.TaskRepositoryMock
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = UserId("321")
    private val command = Command.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = TaskRepositoryMock(
        invokeCreateTask = {
            DbTaskResponse(
                isSuccess = true,
                data = Task(
                    id = TaskId(uuid),
                    title = it.task.title,
                    description = it.task.description,
                    ownerId = userId,
                    priority = it.task.priority,
                    status = it.task.status
                )
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = TaskProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            taskRequest = Task(
                title = "abc",
                description = "abc",
                priority = Priority.HIGH,
                status = Status.IN_PROGRESS
            ),
        )
        processor.exec(ctx)
        println(ctx.taskResponse)
        println(ctx.appErrors)
        assertEquals(State.FINISHING, ctx.state)
        assertNotEquals(TaskId.NONE, ctx.taskResponse.id)
        assertEquals("abc", ctx.taskResponse.title)
        assertEquals("abc", ctx.taskResponse.description)
        assertEquals(Priority.HIGH, ctx.taskResponse.priority)
        assertEquals(Status.IN_PROGRESS, ctx.taskResponse.status)
    }
}
