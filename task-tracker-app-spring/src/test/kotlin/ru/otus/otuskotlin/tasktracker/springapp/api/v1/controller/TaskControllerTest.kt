package ru.otus.otuskotlin.tasktracker.springapp.api.v1.controller

import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.mappers.v1.*

@WebFluxTest(TaskController::class)
class TaskControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockkBean(relaxUnitFun = true)
    private lateinit var processor: TaskProcessor

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun createTask() = testStubTask(
        url = "/v1/task/create",
        requestObj = TaskCreateRequest(),
        responseObj = Context().toTransportCreate()
    )

    @Test
    fun readTask() = testStubTask(
        url = "/v1/task/read",
        requestObj = TaskReadRequest(),
        responseObj = Context().toTransportRead()
    )

    @Test
    fun updateTask() = testStubTask(
        url = "/v1/task/update",
        requestObj = TaskUpdateRequest(),
        responseObj = Context().toTransportUpdate()
    )

    @Test
    fun deleteTask() = testStubTask(
        url = "/v1/task/delete",
        requestObj = TaskDeleteRequest(),
        responseObj = Context().toTransportDelete()
    )

    @Test
    fun searchTask() = testStubTask(
        url = "/v1/task/search",
        requestObj = TaskSearchRequest(),
        responseObj = Context().toTransportSearch()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubTask(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val expectedResponse =  mapper.readValue(mapper.writeValueAsString(responseObj as IResponse), IResponse::class.java)
                Assertions.assertThat(it).isEqualTo(expectedResponse)
            }
        coVerify { processor.exec(any()) }
    }
}