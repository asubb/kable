package io.github.asubb.kable

fun main() {
    // Test the ping module
    val pingModule = ansibleBuiltin.ping()
    println("Ping module YAML:")
    println(pingModule.toYaml())
    println()

    // Test the debug module
    val debugModule = ansibleBuiltin.debug("Hello world")
    println("Debug module YAML:")
    println(debugModule.toYaml())
    println()

    // Test a playbook with both modules
    val playbook = Playbook().apply {
        name = "My first play"
        hosts = "myhosts"
        task("Ping my hosts") {
            +ansibleBuiltin.ping()
        }
        task("Print message") {
            +ansibleBuiltin.debug("Hello world")
        }
    }
    println("Playbook YAML:")
    println(playbook.toYaml())
}