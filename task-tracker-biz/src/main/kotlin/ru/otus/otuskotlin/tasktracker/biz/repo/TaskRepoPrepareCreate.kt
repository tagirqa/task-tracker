package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        taskRepoRead = taskValidated.deepCopy()
//        taskRepoRead.ownerId = principal.id
        taskRepoPrepare = taskRepoRead

    }
}
