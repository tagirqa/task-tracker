package ru.otus.otuskotlin.tasktracker.common.repo

interface ITaskRepository {
    suspend fun createTask(rq: DbTaskRequest): DbTaskResponse
    suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse
    suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse
    suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse
    suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse
    companion object {
        val NONE = object : ITaskRepository {
            override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
