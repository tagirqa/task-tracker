package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskIdRequest

fun ICorAddExecDsl<Context>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление задачи из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskRepoPrepare)
        val result = taskRepo.deleteTask(request)
        if (!result.isSuccess) {
            state = State.FAILING
            appErrors.addAll(result.appErrors)
        }
        taskRepoDone = taskRepoRead
    }
}
