package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model

import ru.otus.otuskotlin.tasktracker.common.models.Status

enum class TaskStatus {
    TO_DO,
    IN_PROGRESS,
    DONE,
    DELETED
}

fun TaskStatus?.fromTransport() = when(this) {
    null -> Status.NONE
    TaskStatus.TO_DO -> Status.TO_DO
    TaskStatus.IN_PROGRESS -> Status.IN_PROGRESS
    TaskStatus.DONE -> Status.DONE
    TaskStatus.DELETED -> Status.DELETED
}

fun Status.toTransport() = when(this) {
    Status.NONE -> null
    Status.TO_DO -> TaskStatus.TO_DO
    Status.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    Status.DONE -> TaskStatus.DONE
    Status.DELETED -> TaskStatus.DELETED
}