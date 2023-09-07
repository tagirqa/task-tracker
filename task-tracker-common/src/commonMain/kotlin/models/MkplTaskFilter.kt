package ru.otus.otuskotlin.tasktracker.common.models

data class MkplTaskFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var priority: MkplPriority = MkplPriority.NONE,
    var status: MkplStatus = MkplStatus.NONE
)
