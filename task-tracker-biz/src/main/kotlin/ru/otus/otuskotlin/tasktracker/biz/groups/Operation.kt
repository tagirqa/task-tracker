package ru.otus.otuskotlin.tasktracker.biz.groups

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Command
import ru.otus.otuskotlin.tasktracker.common.models.State

fun ICorAddExecDsl<Context>.operation(title: String, command: Command, block: ICorAddExecDsl<Context>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}