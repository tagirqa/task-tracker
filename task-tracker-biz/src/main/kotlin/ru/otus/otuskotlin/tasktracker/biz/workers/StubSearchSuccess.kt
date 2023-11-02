package ru.otus.otuskotlin.tasktracker.biz.workers

import ru.otus.otuskotlin.tasktracker.stub.TaskStub
import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.stubs.Stubs

fun ICorAddExecDsl<Context>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        tasksResponse.addAll(TaskStub.prepareSearchList(taskFilterRequest.status, taskFilterRequest.priority))
    }
}
