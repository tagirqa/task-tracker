package ru.otus.otuskotlin.tasktracker.springapp.service

import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.otus.otuskotlin.tasktracker.common.Context

@Service
class TaskBlockingProcessor {
    private val processor = TaskProcessor()

    fun exec(ctx: Context) = runBlocking { processor.exec(ctx) }
}