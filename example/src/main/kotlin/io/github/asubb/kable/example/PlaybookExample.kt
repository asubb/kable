package io.github.asubb.kable.example

import io.github.asubb.kable.ansible
import io.github.asubb.kable.ansibleBuiltin

/**
 * An example demonstrating how to use the playbook functionality in Kable.
 */
fun main() {
    println("Running Ansible Playbook with Kable...")
    
    // Create and execute an Ansible playbook
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