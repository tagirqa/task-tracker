package ru.otus.otuskotlin.tasktracker.common.helpers

import ru.otus.otuskotlin.tasktracker.common.MkplContext
import ru.otus.otuskotlin.tasktracker.common.models.MkplCommand

fun MkplContext.isUpdatableCommand() =
    this.command in listOf(MkplCommand.CREATE, MkplCommand.UPDATE, MkplCommand.DELETE)
