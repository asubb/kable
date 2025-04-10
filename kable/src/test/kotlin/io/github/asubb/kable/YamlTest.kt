package io.github.asubb.kable

fun main() {
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

    val yaml = playbook.toYaml()
    println("Generated YAML:")
    println(yaml)
    
    // Check if the YAML contains the expected strings
    val expectedStrings = listOf(
        "- name: My first play",
        "  hosts: myhosts",
        "  tasks:",
        "   - name: Ping my hosts",
        "     ansible.builtin.ping:",
        "   - name: Print message",
        "     ansible.builtin.debug:",
        "       msg: Hello world"
    )
    
    for (expected in expectedStrings) {
        val contains = yaml.contains(expected)
        println("Contains '$expected': $contains")
    }
}