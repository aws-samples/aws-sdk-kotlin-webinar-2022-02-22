import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "aws.sdk.kotlin.demo"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("aws.sdk.kotlin:dynamodb:0.13.0-beta")
    implementation("aws.sdk.kotlin:s3:0.13.0-beta")

    implementation("aws.smithy.kotlin:http-client-engine-ktor-jvm:0.7.8")

    implementation("org.slf4j:slf4j-simple:1.7.30")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
