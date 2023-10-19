package ru.otus.otuskotlin.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.errorValidation
import ru.otus.otuskotlin.tasktracker.common.helpers.fail

fun ICorAddExecDsl<Context>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
