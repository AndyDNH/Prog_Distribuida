plugins {
    id("java")
    id("war")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
// Source: https://mvnrepository.com/artifact/jakarta.ws.rs/jakarta.ws.rs-api
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
}

tasks.test {
    useJUnitPlatform()
}