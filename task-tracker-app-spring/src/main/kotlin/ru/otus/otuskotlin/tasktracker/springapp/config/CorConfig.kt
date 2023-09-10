package ru.otus.otuskotlin.tasktracker.springapp.config

import TaskProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CorConfig {
    @Bean
    fun processor() = TaskProcessor()
}