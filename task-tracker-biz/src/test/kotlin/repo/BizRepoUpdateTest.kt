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

class BizRepoUpdateTest {

    private val userId = UserId("321")
    private val command = Command.UPDATE
    private val initTask = Task(
        id = TaskId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        priority = Priority.HIGH,
        status = Status.IN_PROGRESS,
    )
    private val repo by lazy { TaskRepositoryMock(
        invokeReadTask = {
            DbTaskResponse(
                isSuccess = true,
                data = initTask,
            )
        },
        invokeUpdateTask = {
            DbTaskResponse(
                isSuccess = true,
                data = Task(
                    id = TaskId("123"),
                    title = "xyz",
                    description = "xyz",
                    priority = Priority.HIGH,
                    status = Status.DONE,
                )
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
    fun repoUpdateSuccessTest() = runTest {
        val taskToUpdate = Task(
            id = TaskId("123"),
            title = "xyz",
            description = "xyz",
            priority = Priority.HIGH,
            status = Status.DONE,
            lock = TaskLock("123-234-abc-ABC"),
        )
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            taskRequest = taskToUpdate,
        )
        ctx.addTestPrincipal(userId)
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(taskToUpdate.id, ctx.taskResponse.id)
        assertEquals(taskToUpdate.title, ctx.taskResponse.title)
        assertEquals(taskToUpdate.description, ctx.taskResponse.description)
        assertEquals(taskToUpdate.status, ctx.taskResponse.status)
        assertEquals(taskToUpdate.priority, ctx.taskResponse.priority)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
