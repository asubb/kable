package io.github.asubb.kable

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

/**
 * Tests for the Inventory class.
 */
class InventorySpec : DescribeSpec({
    describe("Inventory") {
        context("unaryPlus operator") {
            it("should add a host with default port when no port is specified") {
                // Create an inventory and add a host with the unaryPlus operator
                val inventory = Inventory("myhosts").apply {
                    +"localhost"
                }

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the correct host and default port
                inventoryContent shouldContain "localhost ansible_host=localhost ansible_port=22 ansible_user=root"
            }

            it("should correctly parse host:port format") {
                // Create an inventory with a host in the format hostname:port
                val hostName = "localhost"
                val portNumber = 12345
                val hostString = "$hostName:$portNumber"

                // Create an inventory and add a host with the unaryPlus operator
                val inventory = Inventory("myhosts").apply {
                    +hostString
                }

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the correct host and port
                inventoryContent shouldContain "ansible_host=$hostName"
                inventoryContent shouldContain "ansible_port=$portNumber"
            }

            it("should use default port when port is not a valid number") {
                // Create an inventory with a host in the format hostname:port where port is not a valid number
                val hostName = "localhost"
                val invalidPort = "invalid"
                val hostString = "$hostName:$invalidPort"

                // Create an inventory and add a host with the unaryPlus operator
                val inventory = Inventory("myhosts").apply {
                    +hostString
                }

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the correct host and default port
                inventoryContent shouldContain "ansible_host=$hostName"
                inventoryContent shouldContain "ansible_port=22"
            }

            it("should use the whole string as host when format is not hostname:port") {
                // Create an inventory with a host in a format that is not hostname:port
                val hostString = "localhost:22:extra"

                // Create an inventory and add a host with the unaryPlus operator
                val inventory = Inventory("myhosts").apply {
                    +hostString
                }

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the whole string as both name and host
                inventoryContent shouldContain "$hostString ansible_host=$hostString ansible_port=22 ansible_user=root"
            }
        }

        context("host function") {
            it("should add a host with specified parameters") {
                // Create an inventory and add a host with the host function
                val inventory = Inventory("myhosts")
                inventory.host("host1", "192.168.1.10", 2222, "myuser")

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the correct host, port, and user
                inventoryContent shouldContain "host1 ansible_host=192.168.1.10 ansible_port=2222 ansible_user=myuser"
            }

            it("should use default port and user when not specified") {
                // Create an inventory and add a host with the host function, using default port and user
                val inventory = Inventory("myhosts")
                inventory.host("host1", "192.168.1.10")

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains the correct host and default port and user
                inventoryContent shouldContain "host1 ansible_host=192.168.1.10 ansible_port=22 ansible_user=root"
            }
        }

        context("getContent function") {
            it("should return empty string when no hosts are added") {
                // Create an inventory without adding any hosts
                val inventory = Inventory("myhosts")

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory content is empty
                inventoryContent shouldBe ""
            }

            it("should return hosts separated by newlines when multiple hosts are added") {
                // Create an inventory and add multiple hosts
                val inventory = Inventory("myhosts")
                with(inventory) {
                    +"localhost"
                    host("host1", "192.168.1.10", 2222, "myuser")
                }

                // Get the content of the inventory
                val inventoryContent = inventory.getContent()

                // Check that the inventory contains both hosts separated by newlines
                inventoryContent shouldContain "localhost ansible_host=localhost ansible_port=22 ansible_user=root"
                inventoryContent shouldContain "host1 ansible_host=192.168.1.10 ansible_port=2222 ansible_user=myuser"
                
                // Split the content by newlines and check that there are 2 lines
                val lines = inventoryContent.split("\n")
                lines.size shouldBe 2
            }
        }
    }
})