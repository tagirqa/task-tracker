package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Error
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

fun ICorAddExecDsl<Context>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.BAD_TITLE && state == State.RUNNING }
    handle {
        state = State.FAILING
        this.errors.add(
            Error(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
