package ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions

import ru.otus.otuskotlin.tasktracker.common.models.MkplCommand

class UnknownMkplCommand(command: MkplCommand) : Throwable("Wrong command $command at mapping toTransport stage")
