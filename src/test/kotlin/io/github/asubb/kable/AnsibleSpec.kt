package io.github.asubb.kable

import io.github.asubb.kable.TestEnvironment.Companion.testEnvironment
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

/**
 * Tests for the Ansible DSL.
 * Uses TestContainers with Ubuntu base image for testing.
 * The Ansible commands are executed locally but target the container.
 */
class AnsibleSpec : DescribeSpec({
    lateinit var container: UbuntuHostContainer
    lateinit var testEnv: TestEnvironment

    beforeTest {
        container = UbuntuHostContainer()
        testEnv = testEnvironment {
            +container
        }
        testEnv.start()
    }

    afterTest {
        testEnv.stop()
    }

    describe("Local Ansible command execution targeting container") {
        context("with host pattern but no other parameters") {
            it("should execute successfully and return output") {
                // Execute ansible command locally but target the container
                val result = Ansible {
                    // Target the container using its host and mapped SSH port
                    targetHost(container.host, container.getMappedPort(22))
                    // Configure password authentication
                    password("password")
                    // Disable host key checking for testing
                    disableHostKeyChecking()
                    // Add host pattern (required by Ansible)
                    hostPattern("all")
                    // Use the ping module to check connectivity
                    module("ping")
                }.execute()

                println("RESULT>> [${container.host}:${container.getMappedPort(22)}] $result")
                result.isSuccess shouldBe true
                result.exitCode shouldBe 0
                result.output shouldContain "pong"
            }
        }

        context("with inventory") {
            it("should execute successfully using inventory file with simple format") {
                // Execute ansible command locally but target the container using inventory
                val result = Ansible {
                    // Define inventory with the container host
                    inventory("myhosts") {
                        +"${container.host}:${container.getMappedPort(22)}"
                    }
                    // Configure password authentication
                    password("password")
                    // Disable host key checking for testing
                    disableHostKeyChecking()
                    // Add host pattern (required by Ansible)
                    hostPattern("all")
                    // Use the ping module to check connectivity
                    module("ping")
                }.execute()

                println("RESULT>> [${container.host}:${container.getMappedPort(22)}] $result")
                result.isSuccess shouldBe true
                result.exitCode shouldBe 0
                result.output shouldContain "pong"
            }

            it("should execute successfully using inventory file with detailed format") {
                // Execute ansible command locally but target the container using inventory
                val result = Ansible {
                    // Define inventory with the container host using detailed format
                    inventory("myhosts") {
                        host("host1", container.host, container.getMappedPort(22), "root")
                    }
                    // Configure password authentication
                    password("password")
                    // Disable host key checking for testing
                    disableHostKeyChecking()
                    // Add host pattern (required by Ansible)
                    hostPattern("all")
                    // Use the ping module to check connectivity
                    module("ping")
                }.execute()

                println("RESULT with detailed inventory>> [${container.host}:${container.getMappedPort(22)}] $result")
                result.isSuccess shouldBe true
                result.exitCode shouldBe 0
                result.output shouldContain "pong"
            }
        }
    }
})
