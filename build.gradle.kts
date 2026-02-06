plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.gblins"
version = "1.0.0"
description = "DeviceTracker"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // O CORAÇÃO DO WEBFLUX
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // MONGODB REATIVO
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // KOTLIN & JACKSON
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    // UTILITÁRIOS
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // TESTES REATIVOS
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
