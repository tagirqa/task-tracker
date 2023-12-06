package ru.otus.otuskotlin.tasktracker.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val appErrors: MutableList<AppError> = mutableListOf(),
    var settings: CorSettings = CorSettings.NONE,

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: Task = Task(),
    var taskFilterRequest: TaskFilter = TaskFilter(),

    var taskValidating: Task = Task(),
    var taskFilterValidating: TaskFilter = TaskFilter(),

    var taskValidated: Task = Task(),
    var taskFilterValidated: TaskFilter = TaskFilter(),

    var taskResponse: Task = Task(),
    var tasksResponse: MutableList<Task> = mutableListOf(),

    var taskRepo: ITaskRepository = ITaskRepository.NONE,
    var taskRepoRead: Task = Task(), // То, что прочитали из репозитория
    var taskRepoPrepare: Task = Task(), // То, что готовим для сохранения в БД
    var taskRepoDone: Task = Task(),  // Результат, полученный из БД
    var tasksRepoDone: MutableList<Task> = mutableListOf(),
)
