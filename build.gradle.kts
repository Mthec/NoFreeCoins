plugins {
    java
}

group = "mod.wurmunlimited"
version = "0.1.1"
val shortName = "nofreecoins"
val wurmServerFolder = "E:/Steam/steamapps/common/Wurm Unlimited/WurmServerLauncher/"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.wurmonline:server:1.9")
    implementation("org.gotti.wurmunlimited:server-modlauncher:0.45")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    jar {
        doLast {
            copy {
                from(jar)
                into(wurmServerFolder + "mods/" + shortName)
            }

            copy {
                from("src/main/resources/$shortName.properties")
                into(wurmServerFolder + "mods/")
            }
        }

        includeEmptyDirs = false
        archiveFileName.set("$shortName.jar")

        manifest {
            attributes["Implementation-Version"] = archiveVersion.get()
        }
    }

    register<Zip>("zip") {
        into(shortName) {
            from(jar)
        }

        from("src/main/resources/$shortName.properties")
        archiveFileName.set("$shortName.zip")
    }
}