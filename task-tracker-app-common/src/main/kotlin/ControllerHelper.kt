import kotlinx.datetime.Clock
import ru.otus.otuskotlin.tasktracker.api.logs.mapper.toLog
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.helpers.asTaskTrackerError
import ru.otus.otuskotlin.tasktracker.common.models.State
import kotlin.reflect.KClass

suspend inline fun <T> ITaskAppSettings.controllerHelper(
    crossinline getRequest: suspend Context.() -> Unit,
    crossinline toResponse: suspend Context.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = Context(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.doWithLogging(logId) {
            ctx.getRequest()
            processor.exec(ctx)
            logger.info(
                msg = "Request $logId processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            ctx.state = State.FAILING
            ctx.appErrors.add(e.asTaskTrackerError())
            processor.exec(ctx)
            ctx.toResponse()
        }
    }
}
