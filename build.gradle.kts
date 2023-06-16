import net.minecraftforge.gradle.userdev.UserDevExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.6.21"
}

buildscript {
    repositories {
        maven { url = uri("https://repo.siro256.dev/repository/maven-public/") }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.+") {
            isChanging = true
        }
    }
}

apply(plugin = "net.minecraftforge.gradle")

group = "dev.siro256.rtmpack.siromodels"
version = "0.1.0-SNAPSHOT"
description = "RealTrainMod modelpack by Siro_256"
val projectURL = "https://github.com/Kotatsu-RTM/SiroModels"
val authors = listOf(
    "Siro_256 Twitter: @ffffff_256"
)

repositories {
    maven { url = uri("https://repo.siro256.dev/repository/maven-public/") }
    maven { url = uri("https://cursemaven.com") }
}

configurations.create("includeToJar")

dependencies {
    val fgDepManager = project.extensions[DependencyManagementExtension.EXTENSION_NAME] as DependencyManagementExtension

    implementation("com.github.kotatsu-rtm.kotatsulib:KotatsuLib-mc1_12_2:0.0.1-SNAPSHOT")

    add("minecraft", "net.minecraftforge:forge:1.12.2-14.23.5.2860")

    implementation(fgDepManager.deobf("curse.maven:ngtlib-288989:3873392"))
    implementation(fgDepManager.deobf("curse.maven:realtrainmod-288988:3873403"))
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

configure<UserDevExtension> {
    mappings("snapshot", "20171003-1.12")
}

val tmpSrc = File(buildDir, "tmpSrc/main/kotlin")

val tokens = mapOf(
    "modId" to project.name.toLowerCase(),
    "modName" to project.name,
    "modVersion" to project.version.toString(),
    "description" to project.description,
    "modPage" to projectURL,
    "authors" to authors.joinToString { "\"$it\"" }
)

tasks {
    create("cloneSource", Copy::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(File(projectDir, "src/main/kotlin/"))
        into(tmpSrc)
        filter<ReplaceTokens>("tokens" to tokens)
    }

    compileKotlin {
        doFirst { source = fileTree(tmpSrc) }
        destinationDirectory.set(File(buildDir, "classes/java/main"))

        dependsOn("cloneSource")
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        filesMatching("assets/siromodels/model_jsons/**") {
            name = name.replace(".json", "")
        }

        filesMatching(
            listOf(
                "mcmod.info",
                "assets/siromodels/sounds.json"
            )
        ) {
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }

    withType<KotlinCompile>().configureEach {
        // Common options
        kotlinOptions.allWarningsAsErrors = true

        // Kotlin/JVM compiler options
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
