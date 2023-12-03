package ru.otus.otuskotlin.tasktracker.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.TaskRepositoryMock
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = UserId("321")
    private val command = Command.DELETE
    private val initTask = Task(
        id = TaskId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        priority = Priority.HIGH,
        status = Status.DONE,
        lock = TaskLock("123-234-abc-ABC"),
    )
    private val repo by lazy {
        TaskRepositoryMock(
            invokeReadTask = {
               DbTaskResponse(
                   isSuccess = true,
                   data = initTask,
               )
            },
            invokeDeleteTask = {
                if (it.id == initTask.id)
                    DbTaskResponse(
                        isSuccess = true,
                        data = initTask
                    )
                else DbTaskResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { TaskProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val taskToUpdate = Task(
            id = TaskId("123"),
            lock = TaskLock("123-234-abc-ABC"),
        )
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            taskRequest = taskToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertTrue { ctx.appErrors.isEmpty() }
        assertEquals(initTask.id, ctx.taskResponse.id)
        assertEquals(initTask.title, ctx.taskResponse.title)
        assertEquals(initTask.description, ctx.taskResponse.description)
        assertEquals(initTask.priority, ctx.taskResponse.priority)
        assertEquals(initTask.status, ctx.taskResponse.status)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
