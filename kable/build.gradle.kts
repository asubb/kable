plugins {
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(kotlin("test"))
    implementation("io.kotest:kotest-runner-junit5:5.5.5")
    implementation("io.kotest:kotest-assertions-core:5.5.5")

    // TestContainers
    implementation("org.testcontainers:testcontainers:1.19.3")

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("ch.qos.logback:logback-classic:1.2.11")

    // YAML
    implementation("org.yaml:snakeyaml:2.2")
}

// Configure Maven publishing
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "kable"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("Kable")
                description.set("A Kotlin library for Ansible automation")
                url.set("https://github.com/asubb/kable")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("asubb")
                        name.set("Alexey Subbotin")
                        email.set("alexey@example.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/asubb/kable.git")
                    developerConnection.set("scm:git:ssh://github.com/asubb/kable.git")
                    url.set("https://github.com/asubb/kable")
                }
            }
        }
    }

    repositories {
        maven {
            name = "localRepo"
            url = uri("${buildDir}/repo")
        }
    }
}
