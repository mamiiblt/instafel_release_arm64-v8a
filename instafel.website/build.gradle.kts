import com.github.gradle.node.npm.task.NpmTask
import org.gradle.process.ExecOperations
import javax.inject.Inject

repositories {
    gradlePluginPortal()
}

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    download = false
    nodeProjectDir = file("${layout.projectDirectory}")
    npmInstallCommand = "install"
}

abstract class RunCommandWithArgsTask @Inject constructor(
    private val execOps: ExecOperations
) : DefaultTask() {

    @Input
    lateinit var commandArgs: List<String> 

    @TaskAction
    fun run() {
        execOps.exec {
            commandLine(commandArgs) 
        }
    }
}

tasks.register<RunCommandWithArgsTask>("install") { 
    println("Installing depencendies")
    
    commandArgs = listOf("npm", "install")

    doLast {
        println("Dependencies installed")
        val envFile = File("${layout.projectDirectory}/.env")
        val textToWrite = "SITE_URL=https://instafel.mamiiblt.me"
        envFile.writeText(textToWrite)
        println("Environment veriables writed into .env succesfully.")
    }
}

tasks.register<RunCommandWithArgsTask>("build") {    
    commandArgs = listOf("npm", "run", "build")
}

tasks.register<NpmTask>("lint") {    
    args.set(listOf("run", "lint"))
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