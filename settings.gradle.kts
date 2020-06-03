pluginManagement {
  val kotlinVersion: String by settings
  val intellijPluginVersion: String by settings

  plugins {
    kotlin("jvm") version kotlinVersion apply false
    id("org.jetbrains.intellij") version intellijPluginVersion apply false
  }
}

rootProject.name = "projector-markdown-plugin"
