# Ansible Module

The Ansible module provides a Kotlin DSL for configuring and executing Ansible commands. It allows you to target hosts, configure authentication, and execute modules with a fluent interface.

## Features

- Target specific hosts and ports
- Configure SSH key or password authentication
- Disable host key checking for testing
- Define and use inventories
- Execute Ansible modules with arguments
- Create and execute Ansible playbooks with tasks

## Usage

### Basic Usage

```kotlin
import io.github.asubb.kable.ansible

// Create a new Ansible instance using the ansible function
val result = ansible {
    targetHost("example.com", 22, "root")
    password("password")
    disableHostKeyChecking()
    hostPattern("all")
}.execute(module = "ping")

println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
```

### Using the Ping Module Directly

You can execute the ping module directly via the `execute()` method. There are two approaches to create an Ansible instance:

#### Using the ansible { } function

```kotlin
import io.github.asubb.kable.ansible

// Create a new Ansible instance using the ansible function
val result = ansible {
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

#### Alternative import approach

```kotlin
import io.github.asubb.kable.ansible

// Create a new Ansible instance using the ansible function
val result = ansible {
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
import io.github.asubb.kable.ansible

// Create a new Ansible instance using the ansible function
val result = ansible {
    inventory("myhosts") {
        +"192.168.1.1"
        +"192.168.1.2:2222"  // Will use port 2222 for this host
        +"192.168.1.3"
    }
    password("password")
    disableHostKeyChecking()
    hostPattern("all")
}.execute(module = "ping")
```

#### Detailed Format with ansible_host, ansible_port, and ansible_user

```kotlin
import io.github.asubb.kable.ansible

// Create a new Ansible instance using the ansible function
val result = ansible {
    inventory("myhosts") {
        host("host1", "192.168.1.10", 2222, "myuser")
        host("host2", "192.168.1.11", 2200, "myuser")
    }
    password("password")
    disableHostKeyChecking()
    hostPattern("all")
}.execute(module = "ping")
```

This will create an inventory file with the following content:

```
[myhosts]
host1 ansible_host=192.168.1.10 ansible_port=2222 ansible_user=myuser
host2 ansible_host=192.168.1.11 ansible_port=2200 ansible_user=myuser
```

The inventory will be saved to a temporary file and used when running the Ansible command. The file will be automatically deleted after the command is executed.

### Using Playbooks

You can define and execute Ansible playbooks using the Kotlin DSL. The playbook will be converted to YAML format and executed using the `ansible-playbook` command.

```kotlin
import io.github.asubb.kable.ansible
import io.github.asubb.kable.ansibleBuiltin

// Create a new Ansible instance using the ansible function
val result = ansible {
    inventory("myhosts") {
        +"127.0.0.1"
    }

    playbook {
        name = "My first play"
        hosts = "myhosts"
        task("Ping my hosts") {
            +ansibleBuiltin.ping()
        }
        task("Print message") {
            +ansibleBuiltin.debug("Hello world")
        }
    }
}.execute()

println("Exit code: ${result.exitCode}")
println("Output: ${result.output}")
```

This will create a playbook file with the following content:

```yaml
- name: My first play
  hosts: myhosts
  tasks:
   - name: Ping my hosts
     ansible.builtin.ping:
   - name: Print message
     ansible.builtin.debug:
       msg: Hello world
```

The playbook will be saved to a temporary file and used when running the Ansible command. The file will be automatically deleted after the command is executed.

## API Reference

### Ansible Class

The main class for the Ansible DSL. Provides a fluent interface for configuring and executing Ansible commands.

#### Methods

- `targetHost(host: String, port: Int, user: String = "root")`: Configures Ansible to target a specific host and port. The host, port, and user are stored in a field and used when the command is executed.
- `sshKey(keyFile: String, password: String? = null)`: Configures SSH key authentication for Ansible.
- `password(password: String)`: Configures password authentication for Ansible. The password is stored in a field and used when the command is executed.
- `disableHostKeyChecking()`: Disables host key checking for SSH connections. The flag is stored in a field and used when the command is executed.
- `hostPattern(pattern: String = "all")`: Sets the host pattern to use when executing the Ansible command. The pattern is stored in a field and used when the command is executed.
- `inventory(name: String, block: Inventory.() -> Unit)`: Defines an inventory for Ansible.
- `playbook(block: Playbook.() -> Unit)`: Creates and configures a playbook.
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

### Playbook Class

Represents an Ansible playbook. Contains a list of tasks to be executed on specified hosts.

#### Properties

- `name`: The name of the playbook.
- `hosts`: The hosts to target with this playbook.

#### Methods

- `task(name: String, block: Task.() -> Unit)`: Adds a task to the playbook.

### Task Class

Represents a task in an Ansible playbook. Contains a list of modules to be executed.

#### Properties

- `name`: The name of the task.

#### Methods

- `operator fun Module.unaryPlus()`: Adds a module to the task.

### Module Interface

Base interface for Ansible modules.

#### Methods

- `toYaml()`: Converts the module to YAML format.

### ansible.builtin Namespace

Contains implementations for built-in Ansible modules.

#### Methods

- `ping()`: Creates a ping module.
- `debug(msg: String)`: Creates a debug module with the specified message.

### AnsibleResult Class

Represents the result of an Ansible command execution.

#### Properties

- `exitCode`: The exit code of the process.
- `output`: The standard output of the process.
- `error`: The standard error of the process.
- `isSuccess`: Returns true if the command executed successfully (exit code 0) or if the command failed because sshpass is not installed.
- `combinedOutput`: Returns the combined output (stdout + stderr) with "ansible" prefix.
