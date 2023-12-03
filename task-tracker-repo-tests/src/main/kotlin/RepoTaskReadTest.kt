package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskId
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskIdRequest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoTaskReadTest {
    abstract val repo: ITaskRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.appErrors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.appErrors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<Task> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = TaskId("task-repo-read-notFound")

    }
}
