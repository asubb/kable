package io.github.asubb.kable

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Tests for the Ansible DSL.
 * Uses TestContainers with Ubuntu base image for testing.
 * The Ansible commands are executed locally but target the container.
 */
class AnsibleSpec : DescribeSpec({
    val container = UbuntuHostContainer.INSTANCE

    beforeSpec {
        val host = container.host
        val sshPort = container.getMappedPort(22)
        Socket().use { socket ->
            // Set a timeout to avoid hanging if the port is not reachable
            socket.connect(InetSocketAddress(host, sshPort), 5000)
        }
        println("SSH connection details: $host:$sshPort")
    }
    afterSpec {
        container.stop()
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

                println("RESULT>> $result")
                result.isSuccess shouldBe true
                result.exitCode shouldBe 0
                result.output shouldContain "pong"
            }
        }
    }
})
