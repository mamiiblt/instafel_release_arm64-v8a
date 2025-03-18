plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.6"
}

group = "me.mamiiblt.instafel"
version = "1.5"

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

tasks.shadowJar {
    archiveBaseName = "instafel-gplayapi"
}

tasks.test {
    useJUnitPlatform()
}
