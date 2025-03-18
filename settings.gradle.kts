import java.io.ByteArrayOutputStream

rootProject.name = "me.mamiiblt.instafel"

fun getGitCommitHash(): String {
    val output = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = output
    }
    return output.toString().trim()
}

val commitHash = getGitCommitHash()

gradle.rootProject {
    extensions.extraProperties["commitHash"] = commitHash
}

rootDir.listFiles()
    .filter { it.isDirectory && it.name.startsWith("instafel") && File(it, "build.gradle.kts").exists() }
    .forEach {
        include(it.name)
    }
