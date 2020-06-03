import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

group = "org.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
  jcenter()
  maven { setUrl("https://dl.bintray.com/jetbrains/markdown") }
}

val kotlinVersion: String by project
val markdownParserVersion: String by project
val owaspSanitizerVersion: String by project
val targetJvm: String by project

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
  implementation("org.jetbrains:markdown:$markdownParserVersion")
  implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:$owaspSanitizerVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
  sourceCompatibility = targetJvm
}

tasks.withType<KotlinJvmCompile> {
  kotlinOptions {
    jvmTarget = targetJvm

    freeCompilerArgs = freeCompilerArgs + "-XXLanguage:+NewInference"
  }
}

sourceSets {
  main {
    java {
      srcDirs("src")
      srcDirs("gen")
    }
    resources {
      srcDirs("resource")
    }
  }
  test {
    java {
      srcDirs("test/src")
    }
    resources {
      srcDirs("test/data")
    }
  }
}

intellij {
  version = "2019.3"
  pluginName = "projector-markdown-plugin"
  downloadSources = false
  updateSinceUntilBuild = false
  setPlugins("IntelliLang")
}

// disable bundled markdown plugin in sandbox:
tasks.register("disableBundledMarkdownPlugin") {
  doLast {
    val configDirPath = "build/idea-sandbox/config"
    val configDir = project.file(configDirPath).also {
      it.mkdirs()
    }

    val bundledPluginName = "org.intellij.plugins.markdown"

    project.file("$configDir/disabled_plugins.txt").apply {
      if (!exists()) {
        createNewFile()
      }

      if (bundledPluginName !in readLines()) {
        appendText("org.intellij.plugins.markdown\n")
      }
    }
  }
}

// modify existing task that prepares sandbox:
tasks.named("prepareSandbox") {
  dependsOn("disableBundledMarkdownPlugin")
}
