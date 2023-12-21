package ru.otus.otuskotlin.tasktracker.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.TaskRepositoryMock
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.biz.addTestPrincipal
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = UserId("321")
    private val command = Command.READ
    private val initTask = Task(
        id = TaskId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        priority = Priority.HIGH,
        status = Status.DONE,
    )
    private val repo by lazy { TaskRepositoryMock(
        invokeReadTask = {
            DbTaskResponse(
                isSuccess = true,
                data = initTask,
            )
        }
    ) }
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { TaskProcessor(settings) }

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            taskRequest = Task(
                id = TaskId("123"),
            ),
        )
        ctx.addTestPrincipal(userId)
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(initTask.id, ctx.taskResponse.id)
        assertEquals(initTask.title, ctx.taskResponse.title)
        assertEquals(initTask.description, ctx.taskResponse.description)
        assertEquals(initTask.status, ctx.taskResponse.status)
        assertEquals(initTask.priority, ctx.taskResponse.priority)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
