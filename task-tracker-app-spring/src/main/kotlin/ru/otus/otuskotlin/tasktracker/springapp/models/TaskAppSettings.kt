package ru.otus.otuskotlin.tasktracker.springapp.models

import ITaskAppSettings
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.tasktracker.app.common.AuthConfig
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.logging.common.MpLoggerProvider

@Configuration
data class TaskAppSettings(
    override val corSettings: CorSettings,
    override val processor: TaskProcessor,
    override val logger: MpLoggerProvider,
    override val auth: AuthConfig,
): ITaskAppSettings
