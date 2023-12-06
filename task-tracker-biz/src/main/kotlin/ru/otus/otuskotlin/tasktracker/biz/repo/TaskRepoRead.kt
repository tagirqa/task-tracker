package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskIdRequest


fun ICorAddExecDsl<Context>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение задачи из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskValidated)
        val result = taskRepo.readTask(request)
        val resultTask = result.data
        if (result.isSuccess && resultTask != null) {
            taskRepoRead = resultTask
        } else {
            state = State.FAILING
            appErrors.addAll(result.appErrors)
        }
    }
}
