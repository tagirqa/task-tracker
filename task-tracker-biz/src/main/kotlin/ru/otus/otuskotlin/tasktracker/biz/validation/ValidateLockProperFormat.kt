package ru.otus.otuskotlin.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import ru.otus.otuskotlin.tasktracker.common.Context
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock
import ru.otus.otuskotlin.tasktracker.common.helpers.errorValidation
import ru.otus.otuskotlin.tasktracker.common.helpers.fail

fun ICorAddExecDsl<Context>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { taskValidating.lock != TaskLock.NONE && !taskValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = taskValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
