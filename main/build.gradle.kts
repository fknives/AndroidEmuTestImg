plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
}

application {
    mainClass.set("org.fnives.android.test.dockerfile.Main")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        setEvents(setOf("started", "passed", "skipped", "failed"))
        setExceptionFormat("full")
        showStandardStreams = true
    }
}

dependencies {
    val tomlParserVersion = "1.1.0"
    implementation("cc.ekblad:4koma:$tomlParserVersion")

    val testingJunitJupiterVersion = "5.7.0"
    val testingJunitJupiterRuntimeVersion = "5.3.1"
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$testingJunitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$testingJunitJupiterRuntimeVersion")
}