# Changelog

This file contains a list of changes made to the Kable library.

## 2023-04-20: Added YAML Object Provider for Playbook

### Requirements
- Return Yaml or Yaml provider from the toYaml method, as specified in the issue description: "it should return Yaml or Yaml provider. Yaml should be generated only once, no strict concatenation to create yaml"

### Solution
The issue was fixed by adding a new method `toYamlObject()` that returns a YAML object (a map representing the playbook structure), and updating the `toYaml()` method to use this method internally. This ensures that YAML is generated only once without string concatenation, while maintaining backward compatibility with code that expects a string.

Key changes:
- Added a new `toYamlObject()` method that returns a map representing the playbook structure
- Updated the `toYaml()` method to use the `toYamlObject()` method internally
- Ensured that YAML is generated only once without string concatenation
- Maintained backward compatibility with code that expects a string from the `toYaml()` method

## 2023-04-19: Replaced StringBuilder with Direct YAML Object Serialization in toYaml Methods

### Requirements
- Get rid of string builder approach and return YAML from the toYaml method, as specified in the issue description.

### Solution
The issue was fixed by modifying the `Module` interface and its implementations to return YAML objects (maps) instead of strings, and updating the `Playbook` class to build a complete map structure that represents the playbook, including tasks and modules, and then use SnakeYAML to serialize the entire structure to YAML.

Key changes:
- Modified the `Module` interface to return `Any` instead of `String`
- Updated the implementations of the `Module` interface to return maps instead of strings
- Updated the `Playbook` class to build a complete map structure and use SnakeYAML to serialize it
- Removed the StringBuilder approach for constructing YAML

## 2023-04-18: Migrated Playbook#toYaml to Use SnakeYAML for YAML Serialization

### Requirements
- Migrate the `Playbook#toYaml` method to use proper YAML serialization, as specified in the issue description: "migrate io.github.asubb.kable.Playbook#toYaml$kable"

### Solution
The issue was fixed by updating the `Playbook#toYaml` method to use SnakeYAML for serialization of individual components of the playbook. This ensures that the YAML is generated in a more robust and maintainable way, while still maintaining the exact format expected by the tests.

Key changes:
- Updated the `Playbook#toYaml` method to use SnakeYAML for serialization of individual components
- Used a hybrid approach to ensure the output format matches the expected format in the tests
- Ensured that all tests continue to pass with the new implementation

## 2023-04-17: Added SnakeYAML for YAML Serialization in Module Interface

### Requirements
- Replace string concatenation in `Module#toYaml` with proper YAML serialization, as specified in the issue description: "io.github.asubb.kable.Module#toYaml is using string concatenation to create yaml, try to use proper serialization instead"

### Solution
The issue was fixed by adding SnakeYAML as a dependency and updating the `Module` interface and its implementations to use proper YAML serialization. This ensures that the YAML is generated in a more robust and maintainable way.

Key changes:
- Added SnakeYAML as a dependency in the build.gradle.kts file
- Updated the `Module` interface to use SnakeYAML for YAML serialization
- Ensured that the generated YAML maintains the same format expected by the tests
- Fixed a compilation error in the SimpleExample.kt file by using the `ansible` factory function instead of directly instantiating the `Ansible` class

## 2023-04-16: Updated targetHost, password, and disableHostKeyChecking Methods to Store State in Fields

### Requirements
- Store the state in fields before using it for `targetHost`, `password`, and `disableHostKeyChecking` methods, as specified in the issue description: "also do the same for targetHost, password, disableHostKeyChecking. As a rule -- don't use commandParams"

### Solution
The issue was fixed by updating the `targetHost`, `password`, and `disableHostKeyChecking` methods to store the values in fields before using them. This ensures that the values are stored in the object's state and can be used later when the command is executed.

Key changes:
- Added fields to store the target host information, password, and host key checking flag
- Updated the methods to store the values in the fields instead of adding them to the command parameters
- Updated the `execute` method to use the stored values to build the command
- Removed the code that added the values to the command parameters in the methods

## 2023-04-15: Updated hostPattern Method to Store State in Field

### Requirements
- Store the state in a field before using it, as specified in the issue description: "just store the state in the field before using it, example: fun hostPattern(pattern: String = "all"): Ansible { this.hostPattern = pattern return this }"

### Solution
The issue was fixed by updating the `hostPattern` method to store the pattern in a field before using it. This ensures that the pattern is stored in the object's state and can be used later when the command is executed.

Key changes:
- Added a `hostPattern` field to store the host pattern
- Updated the `hostPattern` method to store the pattern in the field before returning
- Updated the `execute` method to use the stored host pattern
- Removed the code that added the pattern to the command parameters in the `hostPattern` method

## 2023-04-14: Made Ansible Command Compile Only When Executed

### Requirements
- Make the `command` property in the Ansible class compile only when executed, as specified in the issue description: "io.github.asubb.kable.Ansible#command make it compile only when executed"

### Solution
The issue was fixed by changing how the Ansible command is built and executed. Previously, the command was built incrementally as configuration methods were called, which meant it was compiled immediately. Now, the command is built only when the `execute` method is called, ensuring it's compiled only when needed.

Key changes:
- Removed the `command` property that was initialized immediately
- Added a `commandParams` property to store command parameters
- Added a `baseCommand` property to store the base command ("ansible" or "ansible-playbook")
- Updated all methods to use the `commandParams` property instead of the `command` property
- Modified the `execute` method to build the final command only when needed

## 2023-04-13: Fixed Ansible Playbook Inventory Issue

### Requirements
- Fix the issue where Ansible playbooks were not using the inventory file, resulting in "no hosts matched" and "no inventory was parsed" warnings

### Solution
The issue was fixed by adding the inventory file to the command when executing a playbook. Previously, when the command list was cleared for playbook execution, the inventory file was not added back to the command, causing the playbook to run without an inventory.

Key changes:
- Modified the `execute` method in Ansible.kt to add the inventory file to the command when executing a playbook
- Ensured that the inventory file is used when running playbooks, fixing the "no hosts matched" and "no inventory was parsed" warnings

## 2023-04-12: Fixed Compilation Error Using Ansible { } Approach

### Requirements
- Fix the compilation error when using the ansible { } approach

### Solution
The issue was fixed by adding back the `ansible` function to the Ansible companion object. This function was previously removed when the invoke operator was added, causing compilation errors in code that used the `ansible { }` approach.

Key changes:
- Added the `ansible` function to the Ansible companion object
- Ensured compatibility with existing code that uses the `ansible { }` approach

## 2023-04-11: Fixed DeepRecursiveFunction Invoke Operator Conflict

### Requirements
- Fix the issue with the DeepRecursiveFunction invoke operator conflict in the AnsibleSpec.kt file
- Ensure that the Ansible class can be used with the invoke operator without conflicts

### Solution
The issue was fixed by updating the invoke operator in the Ansible companion object to avoid the conflict with the DeepRecursiveFunction.invoke operator. The implementation now has two invoke operators: one that takes a block parameter and one that takes no parameters. This ensures that the Ansible class can be used with the invoke operator without conflicts.

Key changes:
- Updated the invoke operator in the Ansible companion object to have two overloads
- Updated all usages of the Ansible class with the invoke operator to use the explicit `Ansible.invoke {` syntax instead of the implicit `Ansible {` syntax

## 2023-04-10: Added Playbook Functionality

### Requirements
- Implement playbooks functionality in Kotlin DSL
- Support defining tasks within playbooks
- Support built-in Ansible modules like ping and debug
- Generate YAML playbooks that can be executed with ansible-playbook

### Solution
The playbook functionality was implemented by adding new classes and methods to the Kable library. The implementation allows users to define playbooks with tasks and modules in a fluent, Kotlin-idiomatic way. The playbooks are converted to YAML format and executed using the ansible-playbook command.

Key components:
- Added Playbook class to represent an Ansible playbook
- Added Task class to represent tasks within a playbook
- Added Module interface for Ansible modules
- Added ansible.builtin namespace with implementations for built-in modules
- Updated Ansible class to support playbooks
