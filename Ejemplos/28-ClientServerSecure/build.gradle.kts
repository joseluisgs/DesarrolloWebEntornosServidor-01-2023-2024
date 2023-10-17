plugins {
    id("java")
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Lombook para generar código, poner todo esto para que funcione
    implementation("org.projectlombok:lombok:1.18.28")
    testImplementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // JWT
    implementation("com.auth0:java-jwt:4.2.1")

    // BCcrypt
    implementation("org.mindrot:jbcrypt:0.4")


    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}