package ru.otus.otuskotlin.tasktracker.biz

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import ru.otus.otuskotlin.tasktracker.biz.general.initRepo
import ru.otus.otuskotlin.tasktracker.biz.general.prepareResult
import ru.otus.otuskotlin.tasktracker.biz.groups.operation
import ru.otus.otuskotlin.tasktracker.biz.groups.stubs
import ru.otus.otuskotlin.tasktracker.biz.repo.*
import ru.otus.otuskotlin.tasktracker.biz.validation.*
import ru.otus.otuskotlin.tasktracker.biz.workers.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.models.Command
import ru.otus.otuskotlin.tasktracker.common.models.State
import ru.otus.otuskotlin.tasktracker.common.models.TaskId
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock

class TaskProcessor(
    @Suppress("unused")
    private val corSettings: CorSettings = CorSettings()
) {
    suspend fun exec(ctx: Context) = BusinessChain.exec(ctx.apply { this.settings = this@TaskProcessor.corSettings })

    companion object {
        private val BusinessChain = rootChain<Context> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание задачи", Command.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля") { taskValidating = taskRequest.deepCopy() }
                    worker("Очистка id") { taskValidating.id = TaskId.NONE }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishTaskValidation("Завершение проверок")
                }
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание объявления в БД")
                }
                prepareResult("Подготовка ответа")
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
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == State.RUNNING }
                        handle { taskRepoDone = taskRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { taskValidating.lock = TaskLock(taskValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                    worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { taskValidating.lock = TaskLock(taskValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    finishTaskValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение объявления из БД")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                prepareResult("Подготовка ответа")
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
                repoSearch("Поиск задач в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}