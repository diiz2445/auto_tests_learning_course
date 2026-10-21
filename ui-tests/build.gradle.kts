plugins {
    java
    id("io.qameta.allure")
}

val allureVersion = "2.29.1"

dependencies {
    testImplementation(project(":common"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
    testImplementation("com.codeborne:selenide:7.7.1")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
    testImplementation("io.qameta.allure:allure-selenide")
}

allure {
    version.set(allureVersion)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("smokeUi") {
    group = "verification"
    description = "Smoke UI: @Tag(\"smoke\")"
    useJUnitPlatform {
        includeTags("smoke")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register<Test>("uiTests") {
    group = "verification"
    description = "Все UI-тесты (@Tag UI)"
    useJUnitPlatform {
        includeTags("UI")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register<Test>("vebinarUi") {
    group = "verification"
    description = "UI кейсы вебинара VI (@Tag VebinarUI)"
    useJUnitPlatform {
        includeTags("VebinarUI")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}
