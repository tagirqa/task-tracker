package ru.otus.otuskotlin.tasktracker.springapp.api.v1.controller

import controllerHelper
import ru.otus.otuskotlin.tasktracker.api.v1.models.IRequest
import ru.otus.otuskotlin.tasktracker.api.v1.models.IResponse
import ru.otus.otuskotlin.tasktracker.mappers.v1.fromTransport
import ru.otus.otuskotlin.tasktracker.mappers.v1.toTransportTask
import ru.otus.otuskotlin.tasktracker.springapp.models.TaskAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV1(
    appSettings: TaskAppSettings,
    request: Q,
    clazz: KClass<*>,
    logId: String,
): R = appSettings.controllerHelper(
    { fromTransport(request) },
    { toTransportTask() as R },
    clazz,
    logId,
)

