package ru.otus.otuskotlin.tasktracker.biz.permissions

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.tasktracker.auth.resolveFrontPermissions
import ru.otus.otuskotlin.tasktracker.auth.resolveRelationsTo
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.State


fun ICorAddExecDsl<Context>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == State.RUNNING }

    handle {
        taskRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                taskRepoDone.resolveRelationsTo(principal)
            )
        )

        for (ad in tasksRepoDone) {
            ad.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    ad.resolveRelationsTo(principal)
                )
            )
        }
    }
}
