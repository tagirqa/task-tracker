package ru.otus.otuskotlin.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        taskRepoPrepare = taskRepoRead.deepCopy().apply {
            this.title = taskValidated.title
            description = taskValidated.description
            priority = taskValidated.priority
            status = taskValidated.status
            lock = taskValidated.lock
        }
    }
}
