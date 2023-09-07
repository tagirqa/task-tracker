package ru.otus.otuskotlin.tasktracker.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.stubs.MkplStubs

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: MkplTask = MkplTask(),
    var taskFilterRequest: MkplTaskFilter = MkplTaskFilter(),
    var taskResponse: MkplTask = MkplTask(),
    var tasksResponse: MutableList<MkplTask> = mutableListOf(),
)
