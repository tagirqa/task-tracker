package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import ru.otus.otuskotlin.tasktracker.common.models.Priority
import ru.otus.otuskotlin.tasktracker.common.models.Status
import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.UserId
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskFilterRequest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoTaskSearchTest {
    abstract val repo: ITaskRepository

    protected open val initializedObjects: List<Task> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.appErrors)
    }

    @Test
    fun searchPriority() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(priority = Priority.CRITICAL))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.appErrors)
    }

    @Test
    fun searchStatus() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(status = Status.DONE))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[5]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.appErrors)
    }

    companion object: BaseInitAds("search") {

        val searchOwnerId = UserId("owner-124")
        override val initObjects: List<Task> = listOf(
            createInitTestModel("task1"),
            createInitTestModel("task2", ownerId = searchOwnerId),
            createInitTestModel("task3", priority = Priority.CRITICAL, status = Status.DONE),
            createInitTestModel("task4", ownerId = searchOwnerId),
            createInitTestModel("task5", priority = Priority.CRITICAL),
            createInitTestModel("task6", status = Status.DONE),
        )
    }
}
