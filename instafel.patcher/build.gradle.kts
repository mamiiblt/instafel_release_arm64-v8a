plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.6"
}

/************************************************/
/* BUILD CONFIG INITIALIZATION PASHE */

var config = rootProject.extra["instafelConfig"] as Map<*, *>
val projectConfig = config[project.name] as Map<*, *>
val projectVersion = projectConfig["version"] as String
val projectTag = projectConfig["tag"] as String

val commitHash: String by rootProject.extra

group = "me.mamiiblt.instafel"
version = "v$projectVersion-$commitHash-$projectTag"

/************************************************/

repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20240303")
    implementation("commons-io:commons-io:2.18.0")
    implementation("com.android.tools.smali:smali:3.0.9")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.apktool:apktool-lib:2.11.1")
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.3")
}

application {
    mainClass = "me.mamiiblt.instafel.patcher.Patcher"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.register("clear-cache") {
    val filesToDelete = listOf(
        file("${project.projectDir}/bin"),
        file("${project.projectDir}/build"),
        // file("${project.projectDir}/output"),
    )

    delete(filesToDelete)
    doLast {
        println("Cache successfully deleted.")
    }
}

tasks.register("generate-patcher-props") {
    doLast {
        val outputFile = File("${project.projectDir}/src/main/resources/patcher.properties")
        outputFile.writeText("""
            patcher.version=$projectVersion
            patcher.commit=$commitHash
            patcher.tag=$projectTag
        """.trimIndent())

        println("Patcher property file created")
    }

    mustRunAfter("clear-cache")

}

tasks.shadowJar {
    archiveBaseName = "ifl-patcher"
    archiveClassifier = ""
    destinationDirectory.set(file("${project.projectDir}/output"))

    doLast {
        println("JAR generated.")
    }

    mustRunAfter("generate-patcher-props")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.register("build-jar") {
    dependsOn("clear-cache", "generate-patcher-props", "shadowJar")

    doLast {
        delete(file("${project.projectDir}/build"))
        delete(file("${project.projectDir}/bin"))
        println("Temp build caches cleared.")
        println("All tasks completed succesfully")
    }
}