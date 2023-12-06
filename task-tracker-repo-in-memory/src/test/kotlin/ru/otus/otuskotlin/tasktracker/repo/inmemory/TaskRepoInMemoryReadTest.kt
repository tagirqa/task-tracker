package ru.otus.otuskotlin.tasktracker.repo.inmemory

import ru.otus.otuskotlin.tasktracker.backend.repo.tests.RepoTaskReadTest
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository

class TaskRepoInMemoryReadTest: RepoTaskReadTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}
