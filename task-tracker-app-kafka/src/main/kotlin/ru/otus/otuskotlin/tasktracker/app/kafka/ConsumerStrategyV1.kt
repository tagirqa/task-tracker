package ru.otus.otuskotlin.tasktracker.app.kafka

import apiV1RequestDeserialize
import apiV1ResponseSerialize
import ru.otus.otuskotlin.tasktracker.api.v1.models.IRequest
import ru.otus.otuskotlin.tasktracker.api.v1.models.IResponse
import ru.otus.otuskotlin.tasktracker.common.Context
import ru.otus.otuskotlin.tasktracker.mappers.v1.fromTransport
import ru.otus.otuskotlin.tasktracker.mappers.v1.toTransportTask


class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: Context): String {
        val response: IResponse = source.toTransportTask()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: Context) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}