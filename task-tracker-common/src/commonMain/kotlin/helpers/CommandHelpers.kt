package ru.otus.otuskotlin.tasktracker.common.helpers

import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Command

fun Context.isUpdatableCommand() =
    this.command in listOf(Command.CREATE, Command.UPDATE, Command.DELETE)
