package ru.otus.otuskotlin.tasktracker.common.helpers

import ru.otus.otuskotlin.tasktracker.common.MkplContext
import ru.otus.otuskotlin.tasktracker.common.models.MkplError

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkplError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun MkplContext.addError(vararg error: MkplError) = errors.addAll(error)
