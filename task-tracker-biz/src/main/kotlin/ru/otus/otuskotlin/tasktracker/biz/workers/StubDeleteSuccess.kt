package ru.otus.otuskotlin.tasktracker.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs
import ru.otus.otuskotlin.tasktracker.stub.TaskStub

fun ICorAddExecDsl<Context>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        taskResponse = stub
    }
}
