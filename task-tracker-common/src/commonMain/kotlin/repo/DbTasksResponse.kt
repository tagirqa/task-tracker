package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.Task

data class DbTasksResponse(
    override val data: List<Task>?,
    override val isSuccess: Boolean,
    override val appErrors: List<AppError> = emptyList(),
): IDbResponse<List<Task>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTasksResponse(emptyList(), true)
        fun success(result: List<Task>) = DbTasksResponse(result, true)
        fun error(errors: List<AppError>) = DbTasksResponse(null, false, errors)
        fun error(error: AppError) = DbTasksResponse(null, false, listOf(error))
    }
}
