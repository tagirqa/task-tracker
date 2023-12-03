package ru.otus.otuskotlin.tasktracker.repo.inmemory

import ru.otus.otuskotlin.tasktracker.backend.repo.tests.RepoTaskCreateTest

class TaskRepoInMemoryCreateTest : RepoTaskCreateTest() {
    override val repo = TaskRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
