package ru.otus.otuskotlin.tasktracker.common.models

@JvmInline
value class TaskLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TaskLock("")
    }
}