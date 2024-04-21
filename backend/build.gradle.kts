import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"

	id("org.flywaydb.flyway") version "9.22.1"

	kotlin("plugin.lombok") version "1.9.23"
	id("io.freefair.lombok") version "8.1.0"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {

//	starters
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

//    validation
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.webjars:bootstrap:4.6.0")

//    logging
	implementation("org.slf4j:slf4j-api:2.0.9")
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

//	serialization
	implementation("com.google.code.gson:gson:2.8.9")

//	lombok
	compileOnly("org.projectlombok:lombok:1.18.30")

//	jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

//	migration
	implementation("org.flywaydb:flyway-core")

//	kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

//	database
	runtimeOnly("org.postgresql:postgresql")

//	test
	testImplementation("org.springframework.boot:spring-boot-starter-test")

//	test containers
	testImplementation("org.testcontainers:postgresql:1.19.6")
	testImplementation("org.testcontainers:testcontainers:1.19.6")
	testImplementation("org.testcontainers:junit-jupiter:1.19.6")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
