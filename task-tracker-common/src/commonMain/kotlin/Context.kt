package ru.otus.otuskotlin.tasktracker.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<Error> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: Task = Task(),
    var taskFilterRequest: TaskFilter = TaskFilter(),
    var taskResponse: Task = Task(),
    var tasksResponse: MutableList<Task> = mutableListOf(),
)
