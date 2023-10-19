package ru.otus.otuskotlin.tasktracker.common

import ru.otus.otuskotlin.tasktracker.logging.common.MpLoggerProvider


data class CorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
