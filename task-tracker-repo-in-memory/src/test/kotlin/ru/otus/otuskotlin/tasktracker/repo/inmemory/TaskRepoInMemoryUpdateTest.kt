package ru.otus.otuskotlin.tasktracker.repo.inmemory

import ru.otus.otuskotlin.tasktracker.backend.repo.tests.RepoTaskUpdateTest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository

class TaskRepoInMemoryUpdateTest : RepoTaskUpdateTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
