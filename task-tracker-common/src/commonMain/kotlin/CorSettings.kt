package ru.otus.otuskotlin.tasktracker.common

import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import ru.otus.otuskotlin.tasktracker.logging.common.MpLoggerProvider


data class CorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: ITaskRepository = ITaskRepository.NONE,
    val repoTest: ITaskRepository = ITaskRepository.NONE,
    val repoProd: ITaskRepository = ITaskRepository.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
