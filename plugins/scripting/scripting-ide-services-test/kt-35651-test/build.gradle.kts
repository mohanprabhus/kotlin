plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        archiveBaseName.set("artifact")
        archiveVersion.set("1")
    }
}

sourceSets {
    "main" { projectDefault() }
}
