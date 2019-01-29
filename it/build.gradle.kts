group = "io.github.ncomet"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
    }
}

sourceSets {
    getByName("test").resources.srcDirs("src/test/kotlin").exclude("**/*.kt")
}

plugins {
    kotlin("jvm") version "1.3.20"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testCompile("com.intuit.karate:karate-junit5:0.9.1")
    testCompile("com.intuit.karate:karate-apache:0.9.1")
}

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    systemProperty("karate.option", systemProperties["karate.options"] ?: "")
    systemProperty("karate.env", systemProperties["karate.env"] ?: "")
    outputs.upToDateWhen { false }
}