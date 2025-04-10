# Examples

This document provides examples of using the Kable library based on the tests.

## Ansible Examples

### Basic Ansible Command

This example shows how to execute a basic Ansible command targeting a specific host:

```kotlin
import io.github.asubb.kable.ansible

// Execute ansible command locally but target a remote host
val result = ansible {
    // Target the host using its host and SSH port
    targetHost("example.com", 22)
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "ping")

// Check the result
println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
if (result.isSuccess) {
    println("Command executed successfully")
}
```

### Using Inventory

This example shows how to define an inventory and use it when running Ansible commands. There are two ways to add hosts to an inventory:

#### Simple Format

```kotlin
import io.github.asubb.kable.ansible

// Execute ansible command locally but target hosts defined in inventory
val result = ansible {
    // Define inventory with hosts using simple format
    inventory("myhosts") {
        +"192.168.1.1:22"  // The port (22) will be extracted from the string
        +"192.168.1.2:2222"  // Using a different port (2222)
        +"192.168.1.3"  // Default port (22) will be used
    }
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "ping")

// Check the result
println("Result with inventory: ${result.combinedOutput}")
if (result.isSuccess) {
    println("Command executed successfully")
}
```

#### Detailed Format with ansible_host, ansible_port, and ansible_user

```kotlin
import io.github.asubb.kable.ansible

// Execute ansible command locally but target hosts defined in inventory
val result = ansible {
    // Define inventory with hosts using detailed format
    inventory("myhosts") {
        host("host1", "192.168.1.10", 2222, "myuser")
        host("host2", "192.168.1.11", 2200, "myuser")
    }
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "ping")

// Check the result
println("Result with detailed inventory: ${result.combinedOutput}")
if (result.isSuccess) {
    println("Command executed successfully")
}
```

This will create an inventory file with the following content:

```
[myhosts]
host1 ansible_host=192.168.1.10 ansible_port=2222 ansible_user=myuser
host2 ansible_host=192.168.1.11 ansible_port=2200 ansible_user=myuser
```

### Using the Ping Module Directly

This example shows how to use the ping module directly via the `execute()` method:

```kotlin
import io.github.asubb.kable.ansible

// Execute ansible command with ping module directly
val result = ansible {
    // Define inventory with hosts
    inventory("myhosts") {
        +"127.0.0.1"
    }
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "ping")

// Check the result
println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
if (result.isSuccess) {
    println("Command executed successfully")
}
```

This is equivalent to running the following Ansible command:
```shell
ansible myhosts -m ping -i inventory.ini
```

### Using Module with Arguments

This example shows how to use a module with arguments:

```kotlin
import io.github.asubb.kable.ansible

// Execute ansible command with module arguments
val result = ansible {
    // Target the host using its host and SSH port
    targetHost("example.com", 22)
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "shell", args = mapOf(
    "cmd" to "echo 'Hello, World!'"
))

// Check the result
println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
if (result.isSuccess) {
    println("Command executed successfully")
}
```

## Using with TestContainers

The examples above can be used with TestContainers for testing. Here's an example from the tests:

```kotlin
import io.github.asubb.kable.ansible
import io.github.asubb.kable.UbuntuHostContainer
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

// Get a reference to the container
val container = UbuntuHostContainer()
container.start()

// Execute ansible command locally but target the container
val result = ansible {
    // Target the container using its host and mapped SSH port
    targetHost(container.host, container.getMappedPort(22))
    // Configure password authentication
    password("password")
    // Disable host key checking for testing
    disableHostKeyChecking()
    // Add host pattern (required by Ansible)
    hostPattern("all")
}.execute(module = "ping")

// Check the result
result.isSuccess shouldBe true
result.exitCode shouldBe 0
result.output shouldContain "pong"

container.stop()
```
