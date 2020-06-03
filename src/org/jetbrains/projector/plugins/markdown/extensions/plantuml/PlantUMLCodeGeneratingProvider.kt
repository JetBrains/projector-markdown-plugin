/*
 * MIT License
 *
 * Copyright (c) 2019-2020 JetBrains s.r.o.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jetbrains.projector.plugins.markdown.extensions.plantuml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.projector.plugins.markdown.extensions.MarkdownCodeFenceCacheableProvider
import org.jetbrains.projector.plugins.markdown.settings.MarkdownSettingsConfigurable
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownCodeFencePluginCache.MARKDOWN_FILE_PATH_KEY
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownCodeFencePluginCacheCollector
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownUtil
import java.io.File
import java.io.IOException
import java.net.URLClassLoader

internal class PlantUMLProvider(private var cacheCollector: MarkdownCodeFencePluginCacheCollector?) : MarkdownCodeFenceCacheableProvider {
  // this empty constructor is needed for the component initialization
  constructor() : this(null)

  override fun generateHtml(text: String): String {
    val newDiagramFile = File(
      "${getCacheRootPath()}${File.separator}" +
      "${MarkdownUtil.md5(cacheCollector?.file?.path, MARKDOWN_FILE_PATH_KEY)}${File.separator}" +
      "${MarkdownUtil.md5(text, "plantUML-diagram")}.png"
    )

    cacheDiagram(newDiagramFile.absolutePath, text)
    cacheCollector?.addAliveCachedFile(newDiagramFile)

    return "<img src=\"${newDiagramFile.toURI()}\"/>"
  }

  private fun cacheDiagram(newDiagramPath: String, text: String) {
    if (!FileUtil.exists(newDiagramPath)) generateDiagram(text, newDiagramPath)
  }

  @Throws(IOException::class)
  private fun generateDiagram(text: CharSequence, diagramPath: String) {
    var innerText: String = text.toString().trim()
    if (!innerText.startsWith("@startuml")) innerText = "@startuml\n$innerText"
    if (!innerText.endsWith("@enduml")) innerText += "\n@enduml"

    FileUtil.createParentDirs(File(diagramPath))
    storeDiagram(innerText, diagramPath)
  }

  override fun isApplicable(language: String): Boolean = (language == "puml" || language == "plantuml")
                                                         && MarkdownSettingsConfigurable.isPlantUMLAvailable()

  companion object {
    private val LOG = Logger.getInstance(PlantUMLCodeFenceLanguageProvider::class.java)

    private val sourceStringReader by lazy {
      try {
        Class.forName(
          "net.sourceforge.plantuml.SourceStringReader", false, URLClassLoader(
          arrayOf(MarkdownSettingsConfigurable.getDownloadedJarPath()?.toURI()?.toURL()), this::class.java.classLoader
        )
        )
      }
      catch (e: Exception) {
        LOG.warn(
          "net.sourceforge.plantuml.SourceStringReader class isn't found in downloaded PlantUML jar. " +
          "Please try to download another PlantUML library version.", e
        )
        null
      }
    }

    private val generateImageMethod by lazy {
      try {
        sourceStringReader?.getDeclaredMethod("generateImage", Class.forName("java.io.File"))
      }
      catch (e: Exception) {
        LOG.warn(
          "'generateImage' method isn't found in the class 'net.sourceforge.plantuml.SourceStringReader'. " +
          "Please try to download another PlantUML library version.", e
        )
        null
      }
    }
  }

  @Throws(IOException::class)
  private fun storeDiagram(source: String, fileName: String) {
    try {
      generateImageMethod?.invoke(sourceStringReader?.getConstructor(String::class.java)?.newInstance(source), File(fileName))
    }
    catch (e: Exception) {
      LOG.warn("Cannot save diagram PlantUML diagram. ", e)
    }
  }
}
