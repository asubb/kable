package io.github.asubb.kable

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile


val customImage: ImageFromDockerfile = ImageFromDockerfile()
    .withDockerfileFromBuilder {
        it.from("ubuntu:latest")
            .run("""
                apt-get update && 
                apt-get install -y openssh-server sshpass && 
                mkdir -p /run/sshd && 
                echo 'PermitRootLogin yes' >> /etc/ssh/sshd_config && 
                echo 'PasswordAuthentication yes' >> /etc/ssh/sshd_config && 
                echo 'root:password' | chpasswd
            """.trimIndent())
            .expose(22)
            .entryPoint("/bin/bash -c 'service ssh start && while true; do echo \"Container started and ready for SSH connections\" && sleep 30; done'" )
            .build()
    }

/**
 * TestContainer for Ansible tests.
 */
class UbuntuHostContainer : GenericContainer<UbuntuHostContainer>(customImage) {

    init {
        withExposedPorts(22)
    }

    companion object {
        /**
         * Singleton instance of the container.
         */
        val INSTANCE: UbuntuHostContainer by lazy {
            UbuntuHostContainer().apply {
                start()
                // Follow container output using TestEnvironment after container is started
                TestEnvironment.INSTANCE.followOutput(this, "ubuntu-container")
            }
        }
    }
}
