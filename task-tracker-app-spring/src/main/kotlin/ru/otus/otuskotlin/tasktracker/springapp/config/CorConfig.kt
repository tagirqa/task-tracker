package ru.otus.otuskotlin.tasktracker.springapp.config

import ru.otus.otuskotlin.tasktracker.biz.TaskProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CorConfig {
    @Bean
    fun processor() = TaskProcessor()
}