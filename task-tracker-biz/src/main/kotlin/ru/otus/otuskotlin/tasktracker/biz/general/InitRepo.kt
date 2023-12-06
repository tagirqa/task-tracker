package ru.otus.otuskotlin.tasktracker.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.errorAdministration
import ru.otus.otuskotlin.tasktracker.common.helpers.fail
import ru.otus.otuskotlin.tasktracker.common.models.WorkMode
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository

fun ICorAddExecDsl<Context>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы
        """.trimIndent()
    handle {
        taskRepo = when {
            workMode == WorkMode.TEST -> settings.repoTest
            workMode == WorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != WorkMode.STUB && taskRepo == ITaskRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
