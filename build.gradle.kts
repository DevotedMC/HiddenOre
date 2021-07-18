import com.mineinabyss.kotlinSpice
import com.mineinabyss.sharedSetup


plugins {
	java
	idea
	`maven-publish`
	id("com.github.johnrengelman.shadow")
	id("com.mineinabyss.shared-gradle") version "0.0.6"
}

tasks.compileJava {
	options.release.set(16)
}

sharedSetup()

repositories {
    mavenCentral()
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://repo.mineinabyss.com/releases")
	maven("https://papermc.io/repo/repository/maven-public/") //Paper
}

val serverVersion: String by project
val kotlinVersion: String by project


dependencies {
	compileOnly("io.papermc.paper:paper-api:$serverVersion")
}

publishing {
	mineInAbyss(project) {
		from(components["java"])
	}
}
