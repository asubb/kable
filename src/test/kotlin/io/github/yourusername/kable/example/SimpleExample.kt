package io.github.yourusername.kable.example

import io.github.yourusername.kable.Ansible

/**
 * A simple example demonstrating how to use the Kable library.
 */
fun main() {
    println("Running Ansible with Kable...")
    
    // Create and execute an Ansible command without any parameters
    val result = Ansible().execute()
    
    // Print the result
    println("Exit code: ${result.exitCode}")
    println("Output:")
    println(result.output)
    
    if (result.error.isNotEmpty()) {
        println("Error:")
        println(result.error)
    }
    
    println("Execution ${if (result.isSuccess) "succeeded" else "failed"}")
}