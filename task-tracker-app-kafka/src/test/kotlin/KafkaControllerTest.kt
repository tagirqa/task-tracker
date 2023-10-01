import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.tasktracker.api.v1.models.*
import ru.otus.otuskotlin.tasktracker.app.kafka.AppKafkaConfig
import ru.otus.otuskotlin.tasktracker.app.kafka.AppKafkaConsumer
import ru.otus.otuskotlin.tasktracker.app.kafka.ConsumerStrategyV1
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(TaskCreateRequest(
                        requestId = "11111111-1111-1111-1111-111111111111",
                        task = TaskCreateObject(
                            title = "Some Task",
                            description = "some testing task to check them all",
                            owner = "Anonymous",
                            status = TaskStatus.TO_DO,
                            priority = TaskPriority.CRITICAL
                        ),
                        debug = TaskDebug(
                            mode = TaskRequestDebugMode.STUB,
                            stub = TaskRequestDebugStubs.SUCCESS
                        )
                    ))
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TaskCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("Новая задача", result.task?.title)
        assertEquals("Новая задача с критическим приоритетом", result.task?.description)
        assertEquals(TaskPriority.CRITICAL, result.task?.priority)
        assertEquals(TaskStatus.TO_DO, result.task?.status)
    }

    companion object {
        const val PARTITION = 0
    }
}


