package ru.otus.otuskotlin.tasktracker.biz.permissions

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.auth.checkPermitted
import ru.otus.otuskotlin.tasktracker.auth.resolveRelationsTo
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.fail
import ru.otus.otuskotlin.tasktracker.common.models.AppError
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == State.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        taskRepoRead.principalRelations = taskRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = checkPermitted(command, taskRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(AppError(message = "User is not allowed to perform this operation"))
        }
    }
}
