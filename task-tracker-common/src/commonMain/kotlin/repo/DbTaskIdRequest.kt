package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskId
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock

data class DbTaskIdRequest(
    val id: TaskId,
    val lock: TaskLock = TaskLock.NONE,
) {
    constructor(task: Task): this(task.id, task.lock)
}
