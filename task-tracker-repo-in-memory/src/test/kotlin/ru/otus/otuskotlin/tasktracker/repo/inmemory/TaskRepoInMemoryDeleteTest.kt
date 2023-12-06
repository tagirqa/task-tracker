package ru.otus.otuskotlin.tasktracker.repo.inmemory

import ru.otus.otuskotlin.tasktracker.backend.repo.tests.RepoTaskDeleteTest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository

class TaskRepoInMemoryDeleteTest : RepoTaskDeleteTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}
