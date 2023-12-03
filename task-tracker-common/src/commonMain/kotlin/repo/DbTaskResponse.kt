package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.tasktracker.common.helpers.appErrorEmptyId as taskErrorEmptyId
import ru.otus.otuskotlin.tasktracker.common.helpers.appErrorNotFound as taskErrorNotFound
import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock

data class DbTaskResponse(
    override val data: Task?,
    override val isSuccess: Boolean,
    override val appErrors: List<AppError> = emptyList()
): IDbResponse<Task> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTaskResponse(null, true)
        fun success(result: Task) = DbTaskResponse(result, true)
        fun error(appErrors: List<AppError>, data: Task? = null) = DbTaskResponse(data, false, appErrors)
        fun error(appError: AppError, data: Task? = null) = DbTaskResponse(data, false, listOf(appError))

        val errorEmptyId = error(taskErrorEmptyId)

        fun errorConcurrent(lock: TaskLock, task: Task?) = error(
            errorRepoConcurrency(lock, task?.lock?.let { TaskLock(it.asString()) }),
            task
        )

        val errorNotFound = error(taskErrorNotFound)
    }
}
