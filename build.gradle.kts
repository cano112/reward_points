ext["jooq.version"] = "3.19.3"

plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.jooq.jooq-codegen-gradle") version "3.19.3"
}

group = "com.noster"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-core:10.7.1")
	implementation("org.zalando:problem-spring-web-starter:0.29.1")
	implementation("org.zalando:logbook-spring-boot-starter:3.7.2")

	runtimeOnly("org.flywaydb:flyway-database-postgresql:10.7.1")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("io.rest-assured:spring-mock-mvc:5.4.0")
	testImplementation("uk.co.datumedge:hamcrest-json:0.2")
	testImplementation("org.assertj:assertj-core:3.25.3")

	jooqCodegen("org.postgresql:postgresql")
	jooqCodegen("org.jooq:jooq-meta-extensions:3.19.3")
}

jooq {
	configuration {
		logging = org.jooq.meta.jaxb.Logging.INFO
		generator {
			database {
				name = "org.jooq.meta.extensions.ddl.DDLDatabase"
				properties {
					property {
						key = "scripts"
						value = "src/main/resources/db/migration/*.sql"
					}

					property {
						key = "sort"
						value = "flyway"
					}

					property {
						key = "defaultNameCase"
						value = "lower"
					}
				}
			}
		}
	}
}

tasks.named("compileJava") {
	dependsOn(":jooqCodegen")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test>().configureEach {
	jvmArgs("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
	jvmArgs("--enable-preview")
}
