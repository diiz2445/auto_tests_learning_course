plugins {
    java apply false
    id("io.qameta.allure") version "2.12.0" apply false
}

allprojects {
    group = "com.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            testLogging {
                events("passed", "failed", "skipped")
                showStandardStreams = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }
}

// ---------- Агрегирующие Smoke-задачи ----------

tasks.register("smokeApi") {
    group = "verification"
    description = "Smoke-прогон API-автотестов"
    dependsOn(":api-tests:smokeApi")
}

tasks.register("smokeUi") {
    group = "verification"
    description = "Smoke-прогон UI-автотестов"
    dependsOn(":ui-tests:smokeUi")
}

tasks.register("smoke") {
    group = "verification"
    description = "Полный Smoke-прогон API + UI"
    dependsOn("smokeApi", "smokeUi")
}

tasks.register("runAllTests") {
    group = "verification"
    description = "Запуск всех тестов во всех модулях"
    dependsOn(":api-tests:test", ":ui-tests:test")
}
