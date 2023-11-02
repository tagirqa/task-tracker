package ru.otus.otuskotlin.tasktracker.biz

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import ru.otus.otuskotlin.tasktracker.biz.groups.operation
import ru.otus.otuskotlin.tasktracker.biz.groups.stubs
import ru.otus.otuskotlin.tasktracker.biz.validation.*
import ru.otus.otuskotlin.tasktracker.biz.workers.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.Command
import ru.otus.otuskotlin.tasktracker.common.models.TaskId

class TaskProcessor(
    @Suppress("unused")
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: Context) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<Context> {
            initStatus("Инициализация статуса")

            operation("Создание задачи", Command.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля") { taskValidated = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TaskId.NONE }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishTaskValidation("Завершение проверок")
                }
            }
            operation("Получить задачу", Command.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TaskId(taskValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Изменить задачу", Command.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TaskId(taskValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Удалить задачу", Command.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") {
                        taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TaskId(taskValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Поиск задачи", Command.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { taskFilterValidating = taskFilterRequest.copy() }

                    finishTaskFilterValidation("Успешное завершение процедуры валидации")
                }

            }
        }.build()
    }
}