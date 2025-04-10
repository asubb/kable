package io.github.asubb.kable

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

/**
 * Represents an Ansible playbook.
 * Contains a list of tasks to be executed on specified hosts.
 */
class Playbook {
    /**
     * The name of the playbook.
     */
    var name: String = "Unnamed playbook"

    /**
     * The hosts to target with this playbook.
     */
    var hosts: String = "all"

    private val tasks = mutableListOf<Task>()

    /**
     * Adds a task to the playbook.
     * @param name The name of the task
     * @param block Configuration block for the task
     */
    fun task(name: String, block: Task.() -> Unit) {
        val task = Task(name).apply(block)
        tasks.add(task)
    }

    /**
     * Converts the playbook to YAML format.
     * @return A string representation of the playbook in YAML format
     */
    internal fun toYaml(): String {
        // Build the playbook structure as a map
        val tasksList = mutableListOf<Map<String, Any?>>()
        tasks.forEach { task ->
            val taskMap = mutableMapOf<String, Any?>()
            taskMap["name"] = task.name

            task.modules.forEach { module ->
                val moduleData = module.toYaml()
                taskMap.putAll(moduleData)
            }

            tasksList.add(taskMap)
        }
        // Configure DumperOptions for readability
        val options = DumperOptions().apply {
            indent = 2
            isPrettyFlow = true
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }
        return Yaml(options).dump(
            listOf(
                mapOf(
                    "name" to name,
                    "hosts" to hosts,
                    "tasks" to tasksList
                )
            )
        )
    }

}

/**
 * Represents a task in an Ansible playbook.
 * Contains a list of modules to be executed.
 */
class Task(val name: String) {

    internal val modules = mutableListOf<Module>()

    /**
     * Adds a module to the task.
     */
    operator fun Module.unaryPlus() {
        modules.add(this)
    }
}

/**
 * Namespace for built-in Ansible modules.
 */
object ansibleBuiltin {
    /**
     * Creates a ping module.
     * @return A ping module
     */
    fun ping(): Module = object : Module {
        override fun toYaml(): Map<String, Any?> = mapOf("ansible.builtin.ping" to null)
    }

    /**
     * Creates a debug module.
     * @param msg The message to print
     * @return A debug module
     */
    fun debug(msg: String): Module = object : Module {
        override fun toYaml(): Map<String, Any?> = mapOf("ansible.builtin.debug" to mapOf("msg" to msg))
    }
}

/**
 * Base interface for Ansible modules.
 */
interface Module {
    /**
     * Converts the module to YAML format.
     * @return A map that can be serialized to YAML
     */
    fun toYaml(): Map<String, Any?>
}
