package ru.otus.otuskotlin.tasktracker.common.helpers

import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock

fun Throwable.asTaskTrackerError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AppError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun Context.addError(vararg appError: AppError) = appErrors.addAll(appError)

fun Context.fail(appError: AppError) {
    addError(appError)
    state = State.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: AppError.Level = AppError.Level.ERROR,
) = AppError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: AppError.Level = AppError.Level.ERROR,
) = AppError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: TaskLock,
    actualLock: TaskLock?,
    exception: Exception? = null,
) = AppError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val appErrorNotFound = AppError(
    field = "id",
    message = "Not Found",
    code = "not-found"
)

val appErrorEmptyId = AppError(
    field = "id",
    message = "Id must not be null or blank"
)