package ru.otus.otuskotlin.tasktracker.biz.workers

import ru.otus.otuskotlin.tasktracker.stub.TaskStub
import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Priority
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.models.Status
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

fun ICorAddExecDsl<Context>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            taskRequest.priority.takeIf { it != Priority.NONE }?.also { this.priority = it }
            taskRequest.status.takeIf { it != Status.NONE }?.also { this.status = it }
        }
        taskResponse = stub
    }
}
