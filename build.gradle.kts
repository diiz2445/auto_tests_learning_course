plugins {
    java
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

tasks.test {
    useJUnitPlatform()
}

// Задача 1: запускает все тесты
tasks.register("runAllTests") {
    group = "verification"
    description = "Запускает все тесты в проекте"
    dependsOn(tasks.test)
}

// Задача 2: после тестов пишет сообщение
tasks.register("afterTests") {
    group = "verification"
    description = "Пишет сообщение после завершения тестов"
    dependsOn("runAllTests")
    doLast {
        println("Test run is over")
    }
}


val register = tasks.register<Test>("smokeTest") {
    group = "verification"
    description = "Запуск только тестов с @Tag(\"smoke\")"

    useJUnitPlatform {
        includeTags("smoke")
    }

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    testLogging {
        events("passed", "failed", "standardOut")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
tasks.test {
    useJUnitPlatform {
         includeTags("smoke")

    }
    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.register<Test>("failingTest") {
    group = "verification"
    description = "Запуск только тестов с @Tag(\"failing\")"

    useJUnitPlatform {
        includeTags("failing")
    }

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    testLogging {
        events("passed", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}