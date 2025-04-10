package io.github.asubb.kable

import io.kotest.core.TestConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer

/**
 * Test environment utility class for managing test containers and logging.
 * Provides methods to capture and log container output.
 */
class TestEnvironment private constructor() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TestEnvironment::class.java)

        fun TestConfiguration.testEnvironment(block: TestEnvironment.() -> Unit): TestEnvironment {
            return TestEnvironment().apply(block)
        }
    }

    private val containers = mutableListOf<GenericContainer<*>>()

    operator fun <T : GenericContainer<T>> T.unaryPlus() {
        containers += this
    }

    fun start() {
        containers.forEach {
            it.start()
            followOutput(it, it.containerName)
        }
    }

    fun stop() {
        containers.forEach { it.stop() }
    }

    /**
     * Follows the output of a container and logs it using SLF4J.
     * @param container The container to follow
     * @param prefix Optional prefix to add to log messages
     * @return The container for method chaining
     */
    private fun followOutput(container: GenericContainer<*>, prefix: String = "") {
        val logConsumer = Slf4jLogConsumer(logger).withPrefix(prefix)
        container.followOutput(logConsumer)
        logger.info("Started following output for container: ${container.dockerImageName}")
    }
}
