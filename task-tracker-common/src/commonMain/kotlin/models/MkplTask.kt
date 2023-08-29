package ru.otus.otuskotlin.tasktracker.common.models

data class MkplTask(
    var id: MkplTaskId = MkplTaskId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var priority: MkplPriority = MkplPriority.NONE,
    var status: MkplStatus = MkplStatus.NONE,
    var productId: MkplProductId = MkplProductId.NONE,
    val permissionsClient: MutableSet<MkplTaskPermissionClient> = mutableSetOf()
)
