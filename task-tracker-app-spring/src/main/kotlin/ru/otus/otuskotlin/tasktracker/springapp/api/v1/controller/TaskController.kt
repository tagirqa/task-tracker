package ru.otus.otuskotlin.tasktracker.springapp.api.v1.controller

import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.mappers.v1.*
import ru.otus.otuskotlin.tasktracker.mappers.v1.toTransportUpdate

@RestController
@RequestMapping("v1/task")
class TaskController(private val processor: TaskProcessor) {

    @PostMapping("create")
    suspend fun createTask(@RequestBody request: TaskCreateRequest): TaskCreateResponse {
        val context = Context()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    suspend fun readTask(@RequestBody request: TaskReadRequest): TaskReadResponse {
        val context = Context()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportRead()
    }

    @PostMapping("update")
    suspend fun updateTask(@RequestBody request: TaskUpdateRequest): TaskUpdateResponse {
        val context = Context()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    suspend fun deleteTask(@RequestBody request: TaskDeleteRequest): TaskDeleteResponse {
        val context = Context()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    suspend fun searchTask(@RequestBody request: TaskSearchRequest): TaskSearchResponse {
        val context = Context()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportSearch()
    }

}