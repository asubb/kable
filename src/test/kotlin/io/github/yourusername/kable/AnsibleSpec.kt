package io.github.yourusername.kable

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain

/**
 * Tests for the Ansible DSL.
 */
class AnsibleSpec : DescribeSpec({
    describe("Ansible command execution") {
        context("with no parameters") {
            it("should show help information and return non-zero exit code") {
                // Note: This test assumes Ansible is installed on the system
                val result = Ansible().execute()

                // Ansible without parameters should show help and return non-zero exit code
                // as it requires at least a host pattern
                result.exitCode shouldNotBe 0

                // Either output or error should contain content
                (result.output.isNotEmpty() || result.error.isNotEmpty()) shouldBe true

                // Output or error should contain some reference to Ansible
                val combinedOutput = result.output + result.error
                combinedOutput.lowercase() shouldContain "ansible"
            }
        }
    }
})
