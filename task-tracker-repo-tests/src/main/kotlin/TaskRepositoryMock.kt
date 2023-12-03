package ru.otus.otuskotlin.tasktracker.backend.repo.tests

import ru.otus.otuskotlin.tasktracker.common.repo.*

class TaskRepositoryMock(
    private val invokeCreateTask: (DbTaskRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadTask: (DbTaskIdRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateTask: (DbTaskRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteTask: (DbTaskIdRequest) -> DbTaskResponse = { DbTaskResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchTask: (DbTaskFilterRequest) -> DbTasksResponse = { DbTasksResponse.MOCK_SUCCESS_EMPTY },
): ITaskRepository {
    override suspend fun createTask(rq: DbTaskRequest): DbTaskResponse {
        return invokeCreateTask(rq)
    }

    override suspend fun readTask(rq: DbTaskIdRequest): DbTaskResponse {
        return invokeReadTask(rq)
    }

    override suspend fun updateTask(rq: DbTaskRequest): DbTaskResponse {
        return invokeUpdateTask(rq)
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): DbTaskResponse {
        return invokeDeleteTask(rq)
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): DbTasksResponse {
        return invokeSearchTask(rq)
    }
}
