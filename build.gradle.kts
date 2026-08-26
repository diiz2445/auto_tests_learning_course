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