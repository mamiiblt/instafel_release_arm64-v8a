import com.github.gradle.node.npm.task.NpmTask

repositories {
    gradlePluginPortal()
}

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    download = true
    version = "20.9.0"
    workDir = file("${layout.projectDirectory}/.gradle/nodejs")
    nodeProjectDir = file("${layout.projectDirectory}")
    npmInstallCommand = "install"
}

tasks.register<NpmTask>("install") { 
    println("Installing deps")
    args.set(listOf("install"))

    doLast {
        println("Dependencies installed")
        val envFile = File("${layout.projectDirectory}/.env")
        val textToWrite = "SITE_URL=https://instafel.mamiiblt.me"
        envFile.writeText(textToWrite)
        println("SITE_URL writed into .env file succesfully.")
    }
}


tasks.register<NpmTask>("lint") {    
    args.set(listOf("run", "lint"))
}

tasks.register<NpmTask>("build") {    
    dependsOn("install")
    args.set(listOf("run", "build"))
}

tasks.register("clear-cache") {
    val filesToDelete = listOf(
        file("${project.projectDir}/build"),
        file("${project.projectDir}/node_modules"),
        file("${project.projectDir}/dist"),
        file("${project.projectDir}/package-lock.json"),
        file("${project.projectDir}/.next"),
        file("${project.projectDir}/.gradle"),
        file("${project.projectDir}/.env")
    )

    delete(filesToDelete)
    println("Cache succesfully deleted.")
}