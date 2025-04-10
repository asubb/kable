package io.github.asubb.kable

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class PlaybookSpec : DescribeSpec({
    describe("Playbook") {
        it("should generate correct YAML") {
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
            yaml shouldContain "- name: My first play"
            yaml shouldContain "  hosts: myhosts"
            yaml shouldContain "  tasks:"
            yaml shouldContain "  - name: Ping my hosts"
            yaml shouldContain "    ansible.builtin.ping:"
            yaml shouldContain "  - name: Print message"
            yaml shouldContain "    ansible.builtin.debug:"
            yaml shouldContain "      msg: Hello world"
        }
    }

    describe("Ansible with Playbook") {
        val container = UbuntuHostContainer()
        val testEnv = testEnvironment {
            +container
        }
        beforeTest {
            testEnv.start()
        }
        afterTest {
            testEnv.stop()
        }
        it("should create correct command for playbook execution") {
            val ansible = ansible {
                inventory("myhosts") {
                    +"${container.host}:${container.getMappedPort(22)}"
                }
                password("password")
                disableHostKeyChecking()
                hostPattern("all")

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
            }

            // We can't easily test the actual execution without mocking the process,
            // but we can verify that the command is constructed correctly by checking
            // the debug logs or by exposing the command for testing purposes.
            // For now, we'll just verify that the playbook is created correctly.
            val result = ansible.execute()

            println("RESULT>> [${container.host}:${container.getMappedPort(22)}] $result")
            // The result should indicate success or at least contain "ansible-playbook"
            // in the output or error message
            result.isSuccess shouldBe true
            result.exitCode shouldBe 0
            result.output shouldContain "localhost                  : ok=3    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0"
        }
    }
})
