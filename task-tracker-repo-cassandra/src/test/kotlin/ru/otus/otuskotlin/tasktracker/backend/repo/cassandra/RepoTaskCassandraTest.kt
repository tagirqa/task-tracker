package ru.otus.otuskotlin.tasktracker.backend.repo.cassandra

import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.tasktracker.backend.repo.tests.*
import ru.otus.otuskotlin.tasktracker.common.models.Task
import ru.otus.otuskotlin.tasktracker.common.models.TaskLock
import ru.otus.otuskotlin.tasktracker.common.repo.ITaskRepository
import java.time.Duration

class RepoAdCassandraCreateTest : RepoTaskCreateTest() {
    override val repo: ITaskRepository = TestCompanion.repository(initObjects, "ks_create", lockNew)
}

class RepoAdCassandraDeleteTest : RepoTaskDeleteTest() {
    override val repo: ITaskRepository = TestCompanion.repository(initObjects, "ks_delete", lockOld)
}

class RepoAdCassandraReadTest : RepoTaskReadTest() {
    override val repo: ITaskRepository = TestCompanion.repository(initObjects, "ks_read", TaskLock(""))
}

class RepoAdCassandraSearchTest : RepoTaskSearchTest() {
    override val repo: ITaskRepository = TestCompanion.repository(initObjects, "ks_search", TaskLock(""))
}

class RepoAdCassandraUpdateTest : RepoTaskUpdateTest() {
    override val repo: ITaskRepository = TestCompanion.repository(initObjects, "ks_update", lockNew)
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(initObjects: List<Task>, keyspace: String, lock: TaskLock): RepoTaskCassandra {
        return RepoTaskCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true, randomUuid = { lock.asString() }, initObjects = initObjects
        )
    }
}
