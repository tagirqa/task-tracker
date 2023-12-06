package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.models.Task

data class DbTaskRequest(
    val task: Task
)
