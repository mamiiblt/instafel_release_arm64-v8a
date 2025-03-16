tasks.register<Exec>("install-deps") {
    workingDir = file("$project")
    commandLine("npm", "install")
}

tasks.register<Exec>("build") {
    workingDir = file("$rootDir/nextjs-project")
    commandLine("npm", "run", "build")
}

tasks.register<Exec>("dev") {
    workingDir = file("$rootDir/nextjs-project")
    commandLine("npm", "run", "dev")
}
