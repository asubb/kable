package io.github.yourusername.kable

import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Main class for the Ansible DSL.
 * Provides a fluent interface for configuring and executing Ansible commands.
 */
class Ansible private constructor() {
    private val processBuilder = ProcessBuilder()
    private val command = mutableListOf("ansible")
    
    /**
     * Executes the configured Ansible command.
     * @return The result of the execution
     */
    fun execute(): AnsibleResult {
        val process = processBuilder.command(command).start()
        val exitCode = process.waitFor()
        val output = process.inputStream.bufferedReader().readText()
        val error = process.errorStream.bufferedReader().readText()
        
        return AnsibleResult(exitCode, output, error)
    }
    
    companion object {
        /**
         * Creates a new Ansible DSL instance.
         * @param block Configuration block for the Ansible command
         * @return Configured Ansible instance
         */
        operator fun invoke(block: Ansible.() -> Unit = {}): Ansible {
            return Ansible().apply(block)
        }
    }
}

/**
 * Represents the result of an Ansible command execution.
 * @property exitCode The exit code of the process
 * @property output The standard output of the process
 * @property error The standard error of the process
 */
data class AnsibleResult(
    val exitCode: Int,
    val output: String,
    val error: String
) {
    /**
     * Returns true if the command executed successfully (exit code 0).
     */
    val isSuccess: Boolean
        get() = exitCode == 0
}