plugins {
    alias(libs.plugins.android.application)
}

/************************************************/
/* BUILD CONFIG INITIALIZATION PASHE */

var config = rootProject.extra["instafelConfig"] as Map<*, *>
val projectConfig = config[project.name] as Map<*, *>
val androidConfig = projectConfig["androidConfig"] as Map<*, *>
val keystoreConfig = androidConfig["keystore"] as Map<*, *>
val depsConfig = projectConfig["dependencyConfig"] as Map<*, *>

val projectVersion = projectConfig["version"] as String
val projectTag = projectConfig["tag"] as String 

val commitHash: String by rootProject.extra

group = "me.mamiiblt.instafel"
version = "v$projectVersion-$projectTag-$commitHash"

println("Build configuration info")
println("")
println("pname: ${project.name}")
println("commit: $commitHash")
println("version: $projectVersion")
println("tag: $projectTag")
println("formated: $version")
/************************************************/

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


android {
    namespace = "me.mamiiblt.instafel.updater"
    compileSdk = 34

    // disable include metadata in dep infos
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    defaultConfig {
        applicationId = "me.mamiiblt.instafel.updater"
        minSdk = 26
        targetSdk = 34
        versionCode = androidConfig["versionCode"] as Int
        versionName = "1.0.0-cmmt"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs{
        create("release") {
            storeFile = File(rootDir, keystoreConfig["ksPath"] as String)
            storePassword = keystoreConfig["ksKeyPass"] as String
            keyAlias = keystoreConfig["ksAlias"] as String
            keyPassword = keystoreConfig["ksPass"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        aidl = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.register("generate-app-debug") {
    dependsOn("clear-cache", "assembleDebug")

    doLast {
        // delete(file("${project.projectDir}/build"))
        println("Temo build caches cleared.")
        println("All tasks completed succesfully")
    }
}

tasks.register("generate-app-release") {
    dependsOn("clear-cache", "assembleRelease")

    doLast {
        // delete(file("${project.projectDir}/build"))
        println("Temo build caches cleared.")
        println("All tasks completed succesfully")
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation("dev.rikka.shizuku:api:${depsConfig["shizuku_version"] as String}")
    implementation("dev.rikka.shizuku:provider:${depsConfig["shizuku_version"] as String}")
    implementation("androidx.work:work-runtime:${depsConfig["android_work_version"] as String}")
    implementation("com.squareup.okhttp3:okhttp:${depsConfig["okhttp_version"] as String}")
    implementation("com.github.TTTT55:Material-You-Preferences:${depsConfig["materialyoupreferences_version"] as String}")
    implementation(libs.navigation.ui)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}