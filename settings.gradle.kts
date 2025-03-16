rootProject.name = "instafel"

rootDir.listFiles()
    .filter { it.isDirectory && it.name.startsWith("instafel") && File(it, "build.gradle.kts").exists() }
    .forEach {
        include(it.name)
    }
