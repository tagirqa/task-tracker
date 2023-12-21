import ru.otus.otuskotlin.tasktracker.app.common.AuthConfig
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.logging.common.MpLoggerProvider

interface ITaskAppSettings {
    val processor: TaskProcessor
    val corSettings: CorSettings
    val logger: MpLoggerProvider
    val auth: AuthConfig
}
