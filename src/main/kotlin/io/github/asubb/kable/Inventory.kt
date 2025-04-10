package io.github.asubb.kable

/**
 * Represents a host in an Ansible inventory.
 * @property name The name of the host
 * @property host The IP address or hostname (ansible_host)
 * @property port The SSH port (ansible_port)
 * @property user The SSH user (ansible_user)
 */
data class Host(
    val name: String,
    val host: String,
    val port: Int = 22,
    val user: String = "root"
)

/**
 * Represents an Ansible inventory.
 * Contains a list of hosts that can be targeted by Ansible commands.
 */
class Inventory(val name: String) {
    private val hosts = mutableListOf<Host>()

    /**
     * Adds a host to the inventory.
     * @param host The host to add as a string in the format "hostname" or "hostname:port"
     */
    operator fun String.unaryPlus() {
        if (this.contains(":")) {
            val parts = this.split(":")
            if (parts.size == 2) {
                val hostname = parts[0]
                val port = parts[1].toIntOrNull() ?: 22
                hosts.add(Host(this, hostname, port))
            } else {
                hosts.add(Host(this, this))
            }
        } else {
            hosts.add(Host(this, this))
        }
    }

    /**
     * Adds a host to the inventory with specific parameters.
     * @param name The name of the host
     * @param host The IP address or hostname (ansible_host)
     * @param port The SSH port (ansible_port)
     * @param user The SSH user (ansible_user)
     */
    fun host(name: String, host: String, port: Int = 22, user: String = "root") {
        hosts.add(Host(name, host, port, user))
    }

    /**
     * Returns the hosts in the inventory as a string.
     */
    internal fun getContent(): String {
        return hosts.joinToString("\n") { host ->
            "${host.name} ansible_host=${host.host} ansible_port=${host.port} ansible_user=${host.user}"
        }
    }
}