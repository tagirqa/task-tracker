package ru.otus.otuskotlin.tasktracker.springapp.api.v1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.springapp.models.TaskAppSettings

@RestController
@RequestMapping("v1/task")
class TaskController(private val appSettings: TaskAppSettings) {

    @PostMapping("create")
    suspend fun createTask(@RequestBody request: TaskCreateRequest): TaskCreateResponse {
        return processV1(appSettings, request = request, this::class, "createTask")
    }

    @PostMapping("read")
    suspend fun readTask(@RequestBody request: TaskReadRequest): TaskReadResponse {
        return processV1(appSettings, request = request, this::class, "readTask")
    }

    @PostMapping("update")
    suspend fun updateTask(@RequestBody request: TaskUpdateRequest): TaskUpdateResponse {
        return processV1(appSettings, request = request, this::class, "updateTask")
    }

    @PostMapping("delete")
    suspend fun deleteTask(@RequestBody request: TaskDeleteRequest): TaskDeleteResponse {
        return processV1(appSettings, request = request, this::class, "deleteTask")
    }

    @PostMapping("search")
    suspend fun searchTask(@RequestBody request: TaskSearchRequest): TaskSearchResponse {
        return processV1(appSettings, request = request, this::class, "searchTask")
    }

}