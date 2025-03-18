plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.6"
}

val commitHash: String by rootProject.extra

group = "me.mamiiblt.instafel"
version = "v1.5-$commitHash"
var releaseTag: String = "snapshot"

println("commit: $commitHash")
println("version: $version")

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.gitlab.AuroraOSS:gplayapi:0e224071")
    implementation("org.json:json:20240303")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

application {
    mainClass = "me.mamiiblt.instafel.gplayapi.Main"
}

tasks.register("setTag") {
    val releaseArg = project.findProperty("release") != null

    if (releaseArg) {
        releaseTag = "release"
    }

    doLast {
        version = "$version-$releaseTag"
        println("Version set to: $version")
    }
}

tasks.register("clear-cache") {
    val filesToDelete = listOf(
        file("${project.projectDir}/build"),
        file("${project.projectDir}/output"),
    )

    delete(filesToDelete)
    doLast {
        println("Cache successfully deleted.")
    }

    mustRunAfter("setTag")
}

tasks.shadowJar {
    archiveBaseName = "ifl-gplayapi"
    archiveClassifier = ""
    destinationDirectory.set(file("${project.projectDir}/output"))

    doLast {
        println("JAR generated.")
    }

    mustRunAfter("clear-cache")
}

tasks.register("makeClear") {

    doLast {
        delete(file("${project.projectDir}/build"))
        println("Project cleared.")
    }

    mustRunAfter("shadowJar")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.register("build-jar") {
    dependsOn("setTag", "clear-cache", "shadowJar", "makeClear")

    doLast {
        println("All tasks completed succesfully")
    }
}

tasks.test {
    useJUnitPlatform()
}
