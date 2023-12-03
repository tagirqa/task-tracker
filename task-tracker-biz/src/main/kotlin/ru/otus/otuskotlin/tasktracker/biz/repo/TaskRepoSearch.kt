package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskFilterRequest

fun ICorAddExecDsl<Context>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск задач в БД по фильтру"
    on { state == State.RUNNING }
    handle {
        val request = DbTaskFilterRequest(
            titleFilter = taskFilterValidated.searchString,
            ownerId = taskFilterValidated.ownerId,
            priority = taskFilterValidated.priority,
            status = taskFilterValidated.status
        )
        val result = taskRepo.searchTask(request)
        val resultTasks = result.data
        if (result.isSuccess && resultTasks != null) {
            tasksRepoDone = resultTasks.toMutableList()
        } else {
            state = State.FAILING
            appErrors.addAll(result.appErrors)
        }
    }
}
