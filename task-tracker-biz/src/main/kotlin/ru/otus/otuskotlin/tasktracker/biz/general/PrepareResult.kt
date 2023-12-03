package ru.otus.otuskotlin.tasktracker.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.models.WorkMode


fun ICorAddExecDsl<Context>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        taskResponse = taskRepoDone
        tasksResponse = tasksRepoDone
        state = when (val st = state) {
            State.RUNNING -> State.FINISHING
            else -> st
        }
    }
}
