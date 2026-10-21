plugins {
    java
    id("io.qameta.allure")
}

val allureVersion = "2.29.1"

dependencies {
    testImplementation(project(":common"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
}

allure {
    version.set(allureVersion)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("smokeApi") {
    group = "verification"
    description = "Smoke API: @Tag(\"smoke\")"
    useJUnitPlatform {
        includeTags("smoke")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register<Test>("apiTests") {
    group = "verification"
    description = "Все API-тесты (@Tag API)"
    useJUnitPlatform {
        includeTags("API")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}
