package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

fun ICorAddExecDsl<Context>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.BAD_DESCRIPTION && state == State.RUNNING }
    handle {
        state = State.FAILING
        this.appErrors.add(
            AppError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
