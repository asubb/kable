package io.github.asubb.kable

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer

/**
 * Test environment utility class for managing test containers and logging.
 * Provides methods to capture and log container output.
 */
class TestEnvironment private constructor() {
    private val logger: Logger = LoggerFactory.getLogger(TestEnvironment::class.java)

    /**
     * Follows the output of a container and logs it using SLF4J.
     * @param container The container to follow
     * @param prefix Optional prefix to add to log messages
     * @return The container for method chaining
     */
    fun <T : GenericContainer<T>> followOutput(container: T, prefix: String = ""): T {
        val logConsumer = Slf4jLogConsumer(logger).withPrefix(prefix)
        container.followOutput(logConsumer)
        logger.info("Started following output for container: ${container.dockerImageName}")
        return container
    }

    /**
     * Logs the result of a container execution.
     * @param result The execution result
     * @param commandDescription Description of the command that was executed
     */
    fun logExecResult(result: Container.ExecResult, commandDescription: String) {
        logger.info("Command executed: $commandDescription")
        logger.info("Exit code: ${result.exitCode}")

        if (result.stdout.isNotEmpty()) {
            logger.info("STDOUT:")
            result.stdout.lines().forEach { logger.info("  $it") }
        }

        if (result.stderr.isNotEmpty()) {
            logger.info("STDERR:")
            result.stderr.lines().forEach { logger.info("  $it") }
        }
    }

    /**
     * Logs the result of an Ansible execution.
     * @param result The Ansible execution result
     * @param commandDescription Description of the command that was executed
     */
    fun logExecResult(result: AnsibleResult, commandDescription: String) {
        logger.info("Command executed: $commandDescription")
        logger.info("Exit code: ${result.exitCode}")

        if (result.output.isNotEmpty()) {
            logger.info("STDOUT:")
            result.output.lines().forEach { logger.info("  $it") }
        }

        if (result.error.isNotEmpty()) {
            logger.info("STDERR:")
            result.error.lines().forEach { logger.info("  $it") }
        }

        logger.info("Combined output:")
        result.combinedOutput.lines().forEach { logger.info("  $it") }
    }

    companion object {
        /**
         * Singleton instance of the test environment.
         */
        val INSTANCE: TestEnvironment by lazy {
            TestEnvironment()
        }
    }
}
