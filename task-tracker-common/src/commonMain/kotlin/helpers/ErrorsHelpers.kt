package ru.otus.otuskotlin.tasktracker.common.helpers

import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Error
import ru.otus.otuskotlin.tasktracker.common.models.State

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = Error(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun Context.addError(vararg error: Error) = errors.addAll(error)

fun Context.fail(error: Error) {
    addError(error)
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
    level: Error.Level = Error.Level.ERROR,
) = Error(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)