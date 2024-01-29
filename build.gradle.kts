import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
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
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//    implementation("org.springframework.boot:spring-boot-starter-jdbc")
//    implementation("org.springframework:spring-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // JSON
    implementation("com.google.code.gson:gson")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    // MyBatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Database
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
}

tasks.named<BootRun>("bootRun") {
    systemProperty("spring.profiles.active", "local")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
