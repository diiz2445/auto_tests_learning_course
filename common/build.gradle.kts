plugins {
    `java-library`
    id("io.qameta.allure")
}

val allureVersion = "2.29.1"

dependencies {
    api("io.rest-assured:rest-assured:5.5.0")
    api("org.assertj:assertj-core:3.27.3")
    api("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    api("com.fasterxml.jackson.core:jackson-annotations:2.18.2")

    api(platform("io.qameta.allure:allure-bom:$allureVersion"))
    api("io.qameta.allure:allure-java-commons")
    api("io.qameta.allure:allure-rest-assured")
}

allure {
    version.set(allureVersion)
}
