package ru.otus.otuskotlin.tasktracker.springapp.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.tasktracker.app.common.AuthConfig
import ru.otus.otuskotlin.tasktracker.backend.repo.cassandra.RepoTaskCassandra
import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import ru.otus.otuskotlin.tasktracker.common.CorSettings
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import ru.otus.otuskotlin.tasktracker.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.tasktracker.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.tasktracker.repo.inmemory.TaskRepoInMemory
import ru.otus.otuskotlin.tasktracker.springapp.models.TaskAppSettings

@Suppress("unused")
@Configuration
class CorConfig {
    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun appAuth(): AuthConfig = AuthConfig.NONE

    @Bean
    fun prodRepository() = RepoTaskCassandra(keyspaceName = "test_keyspace", host = "0.0.0.0")

    @Bean
    fun testRepository() = TaskRepoInMemory()

    @Bean
    fun stubRepository() = TaskRepoInMemory()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: ITaskRepository,
        @Qualifier("testRepository") testRepository: ITaskRepository,
        @Qualifier("stubRepository") stubRepository: ITaskRepository,
    ): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repoStub = stubRepository,
        repoTest = testRepository,
        repoProd = prodRepository,
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: TaskProcessor,
        logger: MpLoggerProvider,
        auth: AuthConfig,
    ) = TaskAppSettings(
        corSettings = corSettings,
        processor = processor,
        logger = logger,
        auth = auth,
    )

    @Bean
    fun taskProcessor(corSettings: CorSettings) = TaskProcessor(corSettings = corSettings)
}