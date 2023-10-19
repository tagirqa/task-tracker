package ru.otus.otuskotlin.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.finishTaskValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        taskValidated = taskValidating
    }
}

fun ICorAddExecDsl<Context>.finishTaskFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        taskFilterValidated = taskFilterValidating
    }
}
