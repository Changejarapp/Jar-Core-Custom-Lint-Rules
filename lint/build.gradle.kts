plugins {
    `java-library`
    java
    id("maven-publish")
    id("com.android.lint")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
//    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.lint.api)
    testImplementation(libs.lint.checks)
}
tasks.register<Copy>("createJar") {
    from("build/intermediates/packaged-classes/release/")
    into("libs/jars/")
    include("classes.jar")
    rename("classes.jar", "plugin.jar")
}
val versionName = "0.2"
group = "com.jar.internal.custom_rules_lint"
version = versionName

publishing {
    publications {
        create<MavenPublication>("Maven") {
            groupId = "com.jar.internal.custom_rules_lint"
            artifactId = "jar-lint-plugins"
            version = versionName
            // Replace this with the actual path to your JAR file
            artifact(file("build/libs/lint-${versionName}.jar"))
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Changejarapp/Jar-Core-Custom-Detekt-Plugins")
            credentials {
                username = ""
                password = ""
            }
        }
    }
}
