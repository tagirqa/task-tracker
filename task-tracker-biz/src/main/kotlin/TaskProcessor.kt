import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.models.Command
import ru.otus.otuskotlin.tasktracker.common.models.Priority
import ru.otus.otuskotlin.tasktracker.common.models.Status
import ru.otus.otuskotlin.tasktracker.common.models.WorkMode

class TaskProcessor {
    suspend fun exec(ctx: Context) {
        require(ctx.workMode == WorkMode.STUB) {
            "Currently working only in STUB mode"
        }

        when (ctx.command) {
            Command.SEARCH -> {
                ctx.tasksResponse.addAll(TaskStub.prepareSearchList(
                    status = Status.TO_DO,
                    priority = Priority.CRITICAL
                ))
            }
            else -> {
                ctx.taskResponse = TaskStub.get()
            }
        }
    }
}