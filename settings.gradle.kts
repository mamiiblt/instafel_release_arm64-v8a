import org.gradle.process.ExecResult
import org.gradle.api.tasks.Exec
import org.gradle.api.DefaultTask
import java.io.ByteArrayOutputStream
import java.io.File
import groovy.json.JsonSlurper

rootProject.name = "me.mamiiblt.instafel"

// get latest commit hash

fun getGitCommitHash(): String {
    val output = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = output
    }
    return output.toString().trim()
}


// read instafel configuration file

val configFile = File(rootDir, "config/ifl_config.json")
val jsonData = JsonSlurper().parse(configFile) as Map<*, *>

println("Loaded & exported Instafel project configuration file")

// implement veriables to all projects

gradle.rootProject {
    extra["commitHash"] = getGitCommitHash()
    extra["instafelConfig"] = jsonData
}


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

rootDir.listFiles()
    .filter { it.isDirectory && it.name.startsWith("instafel") && File(it, "build.gradle.kts").exists() }
    .forEach {
        include(it.name)
    }

// apply(from = "instafel.updater/updater.settings.gradle.kts")
