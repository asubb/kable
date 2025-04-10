# Requirements

This document outlines the requirements to run the Kable library.

## System Requirements

- Java 8 or higher
- Kotlin 1.9.0 or higher

## Dependencies

The Kable library has the following dependencies:

- Kotlin Standard Library
- Kotlin Reflection Library
- SLF4J API for logging

For testing:
- Kotest for testing
- TestContainers for container-based testing

## Ansible Requirements

To use the Ansible module, you need to have Ansible installed on your system. The Ansible module uses the `ansible` command-line tool to execute commands.

### Installing Ansible

#### On Ubuntu/Debian:

```bash
sudo apt update
sudo apt install ansible
```

#### On macOS:

```bash
brew install ansible
```

#### On Windows:

Ansible is not natively supported on Windows. You can use Windows Subsystem for Linux (WSL) to run Ansible on Windows.

### SSH Requirements

The Ansible module uses SSH to connect to remote hosts. You need to have SSH installed on your system and the remote hosts must be configured to accept SSH connections.

For password authentication, you need to have `sshpass` installed:

#### On Ubuntu/Debian:

```bash
sudo apt update
sudo apt install sshpass
```

#### On macOS:

```bash
brew install hudochenkov/sshpass/sshpass
```

## Docker Requirements

To use the TestContainers functionality for testing, you need to have Docker installed on your system.

### Installing Docker

#### On Ubuntu/Debian:

```bash
sudo apt update
sudo apt install docker.io
sudo systemctl enable --now docker
```

#### On macOS:

Download and install Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop).

#### On Windows:

Download and install Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop).