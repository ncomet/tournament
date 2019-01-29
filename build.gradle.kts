import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "io.github.ncomet"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.20"))
    }
}

plugins {
    kotlin("jvm") version "1.3.20"
    kotlin("kapt") version "1.3.20"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    id("io.spring.dependency-management") version "1.0.3.RELEASE"
    application
}

application {
    mainClassName = "io.github.ncomet.tournament.TournamentApplicationKt"
}

dependencies {
    api("javax.xml:jaxb-api:2.1")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.sun.xml.bind:jaxb-impl:2.3.2")
    kapt("com.google.dagger:dagger-compiler:2.21")
    compile("com.google.dagger:dagger:2.21")
    compile("io.dropwizard:dropwizard-core:1.3.8")
    compile("com.amazonaws:aws-java-sdk-dynamodb:1.11.490")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")
    testCompile("org.assertj:assertj-core:3.11.1")
    testCompile("org.mockito:mockito-core:2.23.4")
    testCompile("io.dropwizard:dropwizard-testing:1.3.8")
    testImplementation("com.beust:klaxon:5.0.4")
    testImplementation(kotlin("reflect"))
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
        }
    }

    withType<ShadowJar> {
        mergeServiceFiles()
        exclude("META-INF/*.DSA", "META-INF/*.RSA")
        archiveVersion.set("")
        archiveClassifier.set("")
    }
}

repositories {
    mavenCentral()
    jcenter()
}