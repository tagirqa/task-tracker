package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskRequest

fun ICorAddExecDsl<Context>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление задачи в БД"
    on { state == State.RUNNING }
    handle {
        val request = DbTaskRequest(taskRepoPrepare)
        val result = taskRepo.createTask(request)
        val resultTask = result.data
        if (result.isSuccess && resultTask != null) {
            taskRepoDone = resultTask
        } else {
            state = State.FAILING
            appErrors.addAll(result.appErrors)
        }
    }
}
