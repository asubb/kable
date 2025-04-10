dependencies {
    implementation(project(":kable"))
    
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    
    // Logging
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}

// Configure the main class for the application
tasks.register<JavaExec>("runExample") {
    group = "application"
    description = "Run the example application"
    
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.asubb.kable.example.SimpleExampleKt")
}