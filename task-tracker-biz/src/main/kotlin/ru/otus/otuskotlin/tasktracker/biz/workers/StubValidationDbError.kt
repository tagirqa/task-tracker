package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Error
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

fun ICorAddExecDsl<Context>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.DB_ERROR && state == State.RUNNING }
    handle {
        state = State.FAILING
        this.errors.add(
            Error(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
