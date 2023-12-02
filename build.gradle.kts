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

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok:lombok")
    implementation("com.google.code.gson:gson")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework:spring-jdbc")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
//    implementation ("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    runtimeOnly ("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
