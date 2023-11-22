plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.brott.dev/") }
}

dependencies {
    implementation("com.github.NoahBres:MeepMeep:2.0.3")
}