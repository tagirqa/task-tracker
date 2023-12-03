package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskRequest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoTaskCreateTest {
    abstract val repo: ITaskRepository

    protected open val lockNew: TaskLock = TaskLock("20000000-0000-0000-0000-000000000002")

    private val createObj = Task(
        title = "create object",
        description = "create object description",
        ownerId = UserId("owner-123"),
        priority = Priority.CRITICAL,
        status = Status.TO_DO
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTask(DbTaskRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: TaskId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.priority, result.data?.priority)
        assertNotEquals(TaskId.NONE, result.data?.id)
        assertEquals(emptyList(), result.appErrors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<Task> = emptyList()
    }
}
