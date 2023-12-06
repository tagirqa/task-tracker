package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import IInitObjects
import ru.otus.otuskotlin.tasktracker.common.models.*

abstract class BaseInitAds(val op: String): IInitObjects<Task> {
    open val lockOld: TaskLock = TaskLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: TaskLock = TaskLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: UserId = UserId("owner-123"),
        priority: Priority = Priority.NONE,
        status: Status = Status.NONE,
        lock: TaskLock = lockOld,
    ) = Task(
        id = TaskId("task-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        priority = priority,
        status = status,
        lock = lock,
    )
}
