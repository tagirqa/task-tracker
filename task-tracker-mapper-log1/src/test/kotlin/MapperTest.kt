import ru.otus.otuskotlin.tasktracker.api.logs.mapper.toLog
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = Context(
            requestId = RequestId("1234"),
            command = Command.CREATE,
            taskResponse = Task(
                title = "title",
                description = "desc",
                priority = Priority.HIGH,
                status = Status.DONE
            ),
            appErrors = mutableListOf(
                AppError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = State.RUNNING,
        )

        val log = context.toLog("test-id")
        println(log.task)

        assertEquals("test-id", log.logId)
        assertEquals("task-tracker", log.source)
        assertEquals("1234", log.task?.requestId)
        assertEquals("HIGH", log.task?.responseTask?.priority)
        val error = log.errors?.firstOrNull()
        assertEquals("wrong title", error?.message)
        assertEquals("ERROR", error?.level)
    }
}
