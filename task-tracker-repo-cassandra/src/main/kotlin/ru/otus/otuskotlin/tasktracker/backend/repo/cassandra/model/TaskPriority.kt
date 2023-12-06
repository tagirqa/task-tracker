package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.model

import ru.otus.otuskotlin.tasktracker.common.models.Priority

enum class TaskPriority {
    LOW,
    HIGH,
    CRITICAL
}

fun TaskPriority?.fromTransport() = when(this) {
    null -> Priority.NONE
    TaskPriority.LOW -> Priority.LOW
    TaskPriority.HIGH -> Priority.HIGH
    TaskPriority.CRITICAL -> Priority.CRITICAL
}

fun Priority.toTransport() = when(this) {
    Priority.NONE -> null
    Priority.LOW -> TaskPriority.LOW
    Priority.HIGH -> TaskPriority.HIGH
    Priority.CRITICAL -> TaskPriority.CRITICAL
}