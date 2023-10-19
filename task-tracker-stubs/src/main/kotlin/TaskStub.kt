import ru.otus.otuskotlin.tasktracker.common.models.*

object TaskStub {

    fun get(): Task = NEW_CRITICAL_TASK

    fun prepareResult(block: Task.() -> Unit): Task = get().apply(block)

    fun prepareSearchList(status: Status, priority: Priority) = listOf(
        get().copy(
            id = TaskId("task1"),
            status = status,
            priority = priority
        ),
        get().copy(
            id = TaskId("task2"),
            status = status,
            priority = priority
        ),
        get().copy(
            id = TaskId("task3"),
            status = status,
            priority = priority
        )
    )

    val NEW_CRITICAL_TASK: Task
        get() = Task(
            id = TaskId("111"),
            title = "Новая задача",
            description = "Новая задача с критическим приоритетом",
            priority = Priority.CRITICAL,
            status = Status.TO_DO,
            ownerId = UserId("user-1"),
            permissionsClient = mutableSetOf(
                TaskPermissionClient.READ,
                TaskPermissionClient.UPDATE,
                TaskPermissionClient.DELETE
            )
        )
}