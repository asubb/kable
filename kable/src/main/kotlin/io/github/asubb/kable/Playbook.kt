package io.github.asubb.kable

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
     */
    internal fun toYaml(): String {
        val sb = StringBuilder()
        sb.appendLine("- name: $name")
        sb.appendLine("  hosts: $hosts")
        sb.appendLine("  tasks:")

        tasks.forEach { task ->
            sb.appendLine("   - name: ${task.name}")
            task.modules.forEach { module ->
                sb.appendLine("     ${module.toYaml()}")
            }
        }

        return sb.toString()
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
        override fun toYaml(): String = "ansible.builtin.ping:"
    }

    /**
     * Creates a debug module.
     * @param msg The message to print
     * @return A debug module
     */
    fun debug(msg: String): Module = object : Module {
        override fun toYaml(): String = "ansible.builtin.debug:\n       msg: $msg"
    }
}
/**
 * Base interface for Ansible modules.
 */
interface Module {
    /**
     * Converts the module to YAML format.
     */
    fun toYaml(): String
}

/**
 * Namespace for built-in Ansible modules.
 */