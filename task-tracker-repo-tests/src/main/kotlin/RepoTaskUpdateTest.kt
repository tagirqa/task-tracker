package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskRequest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTaskUpdateTest {
    abstract val repo: ITaskRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = TaskId("task-repo-update-not-found")
    protected val lockBad = TaskLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = TaskLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Task(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = UserId("owner-123"),
            priority = Priority.LOW,
            status = Status.DONE,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Task(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = UserId("owner-123"),
        priority = Priority.CRITICAL,
        status = Status.DONE,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        Task(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = UserId("owner-123"),
            priority = Priority.CRITICAL,
            status = Status.DONE,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(reqUpdateSucc.status, result.data?.status)
        assertEquals(reqUpdateSucc.priority, result.data?.priority)
        assertEquals(emptyList(), result.appErrors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.appErrors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.appErrors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<Task> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
