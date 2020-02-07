plugins {
    java
}

buildscript {
    val props = java.util.Properties()
    File("../../gradle.properties").inputStream().buffered().use {
        props.load(it)
    }
    val kotlinVersion = props["bootstrap.kotlin.default.version"]!!
    extra["kotlinVersion"] = kotlinVersion

    repositories {
        mavenLocal()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

apply {
    plugin("kotlin")
}

repositories {
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    mavenLocal()
}

val toolingApiVersion = "6.2.2"
val kotlinVersion: String by extra

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-build-metrics", kotlinVersion))
    implementation("org.gradle:gradle-tooling-api:$toolingApiVersion")
    // The tooling API need an SLF4J implementation available at runtime, replace this with any other implementation
    runtimeOnly("org.slf4j:slf4j-simple:1.7.10")
}
