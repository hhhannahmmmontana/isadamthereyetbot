import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
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

detekt {
    buildUponDefaultConfig = true
    allRules = false
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

application {
    mainClass = "io.github.hhhannahmmmontana.isadamthereyet.BotKt"
}

tasks.test {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "22"
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "22"
}
