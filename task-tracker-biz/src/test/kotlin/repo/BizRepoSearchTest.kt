package ru.otus.otuskotlin.tasktracker.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.TaskRepositoryMock
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTasksResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = UserId("321")
    private val command = Command.SEARCH
    private val initTask = Task(
        id = TaskId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        priority = Priority.HIGH,
        status = Status.DONE,
    )
    private val repo by lazy { TaskRepositoryMock(
        invokeSearchTask = {
            DbTasksResponse(
                isSuccess = true,
                data = listOf(initTask),
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
    fun repoSearchSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            taskFilterRequest = TaskFilter(
                title = "ab",
                status = Status.DONE
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(1, ctx.tasksResponse.size)
    }
}
