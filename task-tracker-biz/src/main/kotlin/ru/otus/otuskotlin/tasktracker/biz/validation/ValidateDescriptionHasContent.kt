package ru.otus.otuskotlin.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.errorValidation
import ru.otus.otuskotlin.tasktracker.common.helpers.fail

fun ICorAddExecDsl<Context>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.description.isNotEmpty() && ! taskValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "description",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
