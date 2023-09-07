package ru.otus.otuskotlin.tasktracker.api.v1

import apiV1Mapper
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = TaskCreateResponse(
        requestId = "123",
        task = TaskResponseObject(
            title = "task title",
            description = "task description",
            owner = "task owner",
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TO_DO
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as TaskCreateResponse

        assertEquals(response, obj)
    }
}
