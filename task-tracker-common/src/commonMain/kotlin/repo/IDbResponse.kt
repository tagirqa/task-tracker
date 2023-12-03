package ru.otus.otuskotlin.tasktracker.common.repo

import ru.otus.otuskotlin.tasktracker.common.models.AppError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val appErrors: List<AppError>
}
