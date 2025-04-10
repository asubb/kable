package io.github.asubb.kable

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Test for the UbuntuHostContainer functionality.
 * Uses a simple socket connection to check if SSH port is available.
 */
class UbuntuContainerSpec : DescribeSpec({
    val container = UbuntuHostContainer()

    val testEnv = testEnvironment {
       +container
    }

    beforeSpec {
        testEnv.start()
    }

    afterSpec {
        testEnv.stop()
    }

    describe("UbuntuHostContainer") {
        context("when started") {
            it("should have SSH port available") {
                val host = container.host
                val sshPort = container.getMappedPort(22)

                // Check if SSH port is available using a simple socket connection
                val isPortAvailable = try {
                    Socket().use { socket ->
                        // Set a timeout to avoid hanging if the port is not reachable
                        socket.connect(InetSocketAddress(host, sshPort), 5000)
                        true
                    }
                } catch (e: IOException) {
                    false
                }


                // Log connection details
                println("SSH connection details: $host:$sshPort")
                println("SSH port available: $isPortAvailable")
                // Assert that the port is available
                isPortAvailable shouldBe true
            }
        }
    }
})
