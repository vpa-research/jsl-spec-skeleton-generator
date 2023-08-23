plugins {
    application
}

group = "org.libsl.skeletons"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.ow2.asm:asm:9.5")
}

application {
    // the main class (entry point) for the application
    mainClass.set("org.libsl.skeletons.JslSpecSkeletonMain")
}

tasks.test {
    useJUnitPlatform()
}