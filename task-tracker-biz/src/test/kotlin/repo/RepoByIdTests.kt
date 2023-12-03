package repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.TaskRepositoryMock
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.*
import ru.otus.otuskotlin.tasktracker.common.repo.DbTaskResponse
import kotlin.test.assertEquals

private val initTask = Task(
    id = TaskId("123"),
    title = "abc",
    description = "abc",
    priority = Priority.HIGH,
    status = Status.IN_PROGRESS
)
private val repo = TaskRepositoryMock(
        invokeReadTask = {
            if (it.id == initTask.id) {
                DbTaskResponse(
                    isSuccess = true,
                    data = initTask,
                )
            } else DbTaskResponse(
                isSuccess = false,
                data = null,
                appErrors = listOf(AppError(message = "Not found", field = "id"))
            )
        }
    )
private val settings by lazy {
    CorSettings(
        repoTest = repo
    )
}
private val processor by lazy { TaskProcessor(settings) }

fun repoNotFoundTest(command: Command) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        taskRequest = Task(
            id = TaskId("12345"),
            title = "xyz",
            description = "xyz",
            priority = Priority.HIGH,
            status = Status.IN_PROGRESS,
            lock = TaskLock("123-234-abc-ABC"),
        ),
    )

    println(ctx.settings.repoTest)
    processor.exec(ctx)
    println(ctx.settings.repoTest)
    assertEquals(State.FAILING, ctx.state)
    assertEquals(Task(), ctx.taskResponse)
    assertEquals(1, ctx.appErrors.size)
    assertEquals("id", ctx.appErrors.first().field)
}
