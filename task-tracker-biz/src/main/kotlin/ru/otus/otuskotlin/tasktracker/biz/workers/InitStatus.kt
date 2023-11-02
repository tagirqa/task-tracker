package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.initStatus(title: String) = worker() {
    this.title = title
    on { state == State.NONE }
    handle { state = State.RUNNING }
}