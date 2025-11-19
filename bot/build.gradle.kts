
plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.jupiter.platform.launcher)
    implementation(libs.guava)

    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)
    implementation(libs.tgbotapi)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

application {
    mainClass = "io.github.hhhannahmmmontana.isadamthereyet.BotKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
