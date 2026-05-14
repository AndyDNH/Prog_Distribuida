plugins {
    id("java")
    id("io.quarkus") version "3.35.2"
    id ("io.freefair.lombok") version "9.2.0"
}

group = "org.example"
version = "unspecified"



repositories {
    mavenCentral()
}

val quarkusVersion =  "3.35.2"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))
//    CDI
    implementation("io.quarkus:quarkus-rest")
//    REST
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest-jsonb")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.modelmapper:modelmapper:3.2.6")
}

tasks.test {
    useJUnitPlatform()
}