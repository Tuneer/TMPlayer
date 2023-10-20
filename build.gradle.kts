import com.android.tools.build.libraries.metadata.MavenRepo

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.1.1" apply false
	id("org.jetbrains.kotlin.android") version "1.9.0" apply false
	id("com.android.library") version "8.1.1" apply false
	id("maven-publish") apply true
}
publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components.findByName("android"))
			
			groupId = "com.tuneer.tmplayer"
			artifactId = "TMlibrary"
			version = "1.0"
		}
	}
}
