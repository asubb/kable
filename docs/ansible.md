# Ansible Module

The Ansible module provides a Kotlin DSL for configuring and executing Ansible commands. It allows you to target hosts, configure authentication, and execute modules with a fluent interface.

## Features

- Target specific hosts and ports
- Configure SSH key or password authentication
- Disable host key checking for testing
- Define and use inventories
- Execute Ansible modules with arguments

## Usage

### Basic Usage

```kotlin
import io.github.asubb.kable.Ansible

// Using the invoke operator defined in the Ansible companion object
val ansible = Ansible()
ansible.targetHost("example.com", 22, "root")
ansible.password("password")
ansible.disableHostKeyChecking()
ansible.hostPattern("all")
val result = ansible.execute(module = "ping")

println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
```

### Using the Ping Module Directly

You can execute the ping module directly via the `execute()` method:

```kotlin
import io.github.asubb.kable.Ansible

// Create a new Ansible instance
val result = Ansible.invoke {
    inventory("myhosts") {
        +"127.0.0.1"
    }
    password("password")
    disableHostKeyChecking()
    hostPattern("all")
}.execute(module = "ping")

println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
```

This is equivalent to running the following Ansible command:
```shell
ansible myhosts -m ping -i inventory.ini
```

### Using Inventory

You can define an inventory with a list of hosts and use it when running Ansible commands. There are two ways to add hosts to an inventory:

#### Simple Format

```kotlin
import io.github.asubb.kable.Ansible

// Using the invoke operator defined in the Ansible companion object
val ansible = Ansible()
ansible.inventory("myhosts") {
    +"192.168.1.1"
    +"192.168.1.2:2222"  // Will use port 2222 for this host
    +"192.168.1.3"
}
ansible.password("password")
ansible.disableHostKeyChecking()
ansible.hostPattern("all")
val result = ansible.execute(module = "ping")
```

#### Detailed Format with ansible_host, ansible_port, and ansible_user

```kotlin
import io.github.asubb.kable.Ansible

// Using the invoke operator defined in the Ansible companion object
val ansible = Ansible()
ansible.inventory("myhosts") {
    host("host1", "192.168.1.10", 2222, "myuser")
    host("host2", "192.168.1.11", 2200, "myuser")
}
ansible.password("password")
ansible.disableHostKeyChecking()
ansible.hostPattern("all")
val result = ansible.execute(module = "ping")
```

This will create an inventory file with the following content:

```
[myhosts]
host1 ansible_host=192.168.1.10 ansible_port=2222 ansible_user=myuser
host2 ansible_host=192.168.1.11 ansible_port=2200 ansible_user=myuser
```

The inventory will be saved to a temporary file and used when running the Ansible command. The file will be automatically deleted after the command is executed.

## API Reference

### Ansible Class

The main class for the Ansible DSL. Provides a fluent interface for configuring and executing Ansible commands.

#### Methods

- `targetHost(host: String, port: Int, user: String = "root")`: Configures Ansible to target a specific host and port.
- `sshKey(keyFile: String, password: String? = null)`: Configures SSH key authentication for Ansible.
- `password(password: String)`: Configures password authentication for Ansible.
- `disableHostKeyChecking()`: Disables host key checking for SSH connections.
- `hostPattern(pattern: String = "all")`: Adds a host pattern to the Ansible command.
- `inventory(name: String, block: Inventory.() -> Unit)`: Defines an inventory for Ansible.
- `execute(module: String? = null, args: Map<String, String> = emptyMap())`: Executes the configured Ansible command. Optionally, you can specify a module to execute directly, such as "ping".

### Host Class

Represents a host in an Ansible inventory.

#### Properties

- `name`: The name of the host.
- `host`: The IP address or hostname (ansible_host).
- `port`: The SSH port (ansible_port).
- `user`: The SSH user (ansible_user).

### Inventory Class

Represents an Ansible inventory. Contains a list of hosts that can be targeted by Ansible commands.

#### Methods

- `operator fun String.unaryPlus()`: Adds a host to the inventory. If the string is in the format "hostname:port", it will extract the hostname and port. Otherwise, it will use the string as both the name and the host.
- `host(name: String, host: String, port: Int = 22, user: String = "root")`: Adds a host to the inventory with specific parameters.

### AnsibleResult Class

Represents the result of an Ansible command execution.

#### Properties

- `exitCode`: The exit code of the process.
- `output`: The standard output of the process.
- `error`: The standard error of the process.
- `isSuccess`: Returns true if the command executed successfully (exit code 0) or if the command failed because sshpass is not installed.
- `combinedOutput`: Returns the combined output (stdout + stderr) with "ansible" prefix.
