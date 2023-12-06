package ru.otus.otuskotlin.tasktracker.common.exceptions

import ru.otus.otuskotlin.tasktracker.common.models.TaskLock

class RepoConcurrencyException(expectedLock: TaskLock, actualLock: TaskLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
