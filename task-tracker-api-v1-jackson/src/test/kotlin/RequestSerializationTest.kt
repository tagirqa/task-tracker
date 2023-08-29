package ru.otus.otuskotlin.tasktracker.api.v1

import apiV1Mapper
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = TaskCreateRequest(
        requestId = "123",
        debug = TaskDebug(
            mode = TaskRequestDebugMode.STUB,
            stub = TaskRequestDebugStubs.BAD_TITLE
        ),
        task = TaskCreateObject(
            title = "task title",
            description = "task description",
            owner = "task owner",
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.IN_PROGRESS
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as TaskCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TaskCreateRequest::class.java)

        assertEquals("123", obj.requestId)
    }
}
