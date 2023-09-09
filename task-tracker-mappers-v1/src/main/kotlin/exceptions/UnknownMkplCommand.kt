package ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions

import ru.otus.otuskotlin.tasktracker.common.models.Command

class UnknownMkplCommand(command: Command) : Throwable("Wrong command $command at mapping toTransport stage")
