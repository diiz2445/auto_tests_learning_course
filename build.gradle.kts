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

    implementation("io.rest-assured:rest-assured:6.0.0")
    // Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.49.0")
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("com.codeborne:selenide:7.17.0")

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
tasks.register<Test>("UI") {
    group = "verification"
    description = "Запуск только тестов с @Tag(\"UI\")"

    useJUnitPlatform {
        includeTags("UI")
    }

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    testLogging {
        events("passed", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
tasks.register<Test>("VebinarUI") {
    group = "verification"
    description = "Запуск только тестов с @Tag(\"VebinarUI\")"

    useJUnitPlatform {
        includeTags("VebinarUI")
    }

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    testLogging {
        events("passed", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
tasks.register<Test>("Testing") {
    group = "verification"
    description = "Запуск только тестов с @Tag(\"UI\")"

    useJUnitPlatform {
        includeTags("testing")
    }

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    testLogging {
        events("passed", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
