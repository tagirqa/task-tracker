package ru.otus.otuskotlin.tasktracker.repo.inmemory

import ru.otus.otuskotlin.tasktracker.backend.repo.tests.RepoTaskSearchTest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository

class TaskRepoInMemorySearchTest : RepoTaskSearchTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}
