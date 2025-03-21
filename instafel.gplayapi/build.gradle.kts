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

println("Build configuration info")
println("")
println("pname: ${project.name}")
println("commit: $commitHash")
println("version: $projectVersion")
println("tag: $projectTag")
println("formated: $version")
/************************************************/

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

tasks.register("clear-cache") {
    val filesToDelete = listOf(
        file("${project.projectDir}/bin"),
        file("${project.projectDir}/build"),
        file("${project.projectDir}/output"),
    )

    delete(filesToDelete)
    doLast {
        println("Cache successfully deleted.")
    }
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

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.register("build-jar") {
    dependsOn("clear-cache", "shadowJar")

    doLast {
        delete(file("${project.projectDir}/build"))
        delete(file("${project.projectDir}/bin"))
        println("Temp build caches cleared.")
        println("All tasks completed succesfully")
    }
}

tasks.test {
    useJUnitPlatform()
}
