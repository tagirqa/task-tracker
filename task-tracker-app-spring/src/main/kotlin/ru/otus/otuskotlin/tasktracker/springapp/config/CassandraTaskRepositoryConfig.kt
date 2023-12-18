package ru.otus.otuskotlin.tasktracker.springapp.config

import com.datastax.oss.driver.api.core.CqlSession
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetAddress
import java.net.InetSocketAddress


@Configuration
class CassandraTaskRepositoryConfig {

    @Bean
    fun cqlSession(): CqlSession {
        return CqlSession.builder()
            .addContactPoint(parseAddresses("0.0.0.0", 9042).first())
            .withLocalDatacenter("datacenter1") // Другие настройки, если необходимо
            .build()
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}