# kable

Kotlin DSL and runner for Ansible

## Overview

Kable is a Kotlin library that provides a DSL (Domain Specific Language) wrapper for Ansible commands. It allows you to write Ansible playbook executions in a type-safe, IDE-friendly Kotlin code instead of using raw command-line arguments.

## Requirements

- JDK 11 or higher
- Ansible installed on the system where the application will run:
  - `sshpass` if using password authentication

## Installation

Add the dependency to your Gradle build file:

```kotlin
dependencies {
    implementation("io.github.yourusername:kable:0.1.0")
}
```

## Usage

Basic usage example:

```kotlin
import io.github.yourusername.kable.*

fun main() {
    // Simple execution of ansible command
    ansible {
        // Configuration and parameters will go here
    }.execute()
}
```

## Features

- Type-safe DSL for Ansible commands
- Integration with Kotlin applications
- Support for all Ansible command-line options

## License

This project is licensed under the MIT License - see the LICENSE file for details.
