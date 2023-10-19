package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.fail
import ru.otus.otuskotlin.tasktracker.common.models.Error
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        fail(
            Error(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
