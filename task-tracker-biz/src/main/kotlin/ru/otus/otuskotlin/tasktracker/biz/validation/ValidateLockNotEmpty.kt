package ru.otus.otuskotlin.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.errorValidation
import ru.otus.otuskotlin.tasktracker.common.helpers.fail


fun ICorAddExecDsl<Context>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
