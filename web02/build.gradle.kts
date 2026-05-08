plugins {
    id("java")
    id("application")
    id("io.freefair.lombok") version "9.2.0"
    id("com.gradleup.shadow") version "9.3.0"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}
var hellidonVersion = "4.4.1"

dependencies {
    implementation("io.helidon.webserver:helidon-webserver:${hellidonVersion}")
    implementation("io.helidon.http.media:helidon-http-media-jsonp:${hellidonVersion}")
    implementation("io.helidon.http.media:helidon-http-media-jsonb:${hellidonVersion}")
    runtimeOnly("io.helidon.config:helidon-config-yaml:${hellidonVersion}")
    implementation("io.helidon.dbclient:helidon-dbclient:${hellidonVersion}")
    implementation("io.helidon.dbclient:helidon-dbclient-jdbc:${hellidonVersion}")
    implementation("io.helidon.dbclient:helidon-dbclient-hikari:${hellidonVersion}")
    runtimeOnly("org.postgresql:postgresql:42.7.3")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.programacion.distribuida.MiAplicacionDistribuida"
    }
}

tasks.shadowJar {
    mergeServiceFiles()
}