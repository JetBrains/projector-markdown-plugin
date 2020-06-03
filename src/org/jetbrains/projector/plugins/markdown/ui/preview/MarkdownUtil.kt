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
package org.jetbrains.projector.plugins.markdown.ui.preview

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.io.DigestUtil
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.annotations.NonNls
import org.jetbrains.projector.plugins.markdown.lang.parser.MarkdownParserManager
import java.io.File
import java.math.BigInteger
import java.util.*

object MarkdownUtil {
  fun md5(buffer: String?, @NonNls key: String): String {
    val md5 = DigestUtil.md5()
    Objects.requireNonNull(md5).update(buffer?.toByteArray(Charsets.UTF_8))
    val code = md5.digest(key.toByteArray(Charsets.UTF_8))
    val bi = BigInteger(code).abs()
    return bi.abs().toString(16)
  }

  fun generateMarkdownHtml(file: VirtualFile, text: String, project: Project?): String {
    val parent = file.parent
    val baseUri = if (parent != null) File(parent.path).toURI() else null

    val parsedTree = MarkdownParser(MarkdownParserManager.FLAVOUR).buildMarkdownTreeFromString(text)
    val cacheCollector = MarkdownCodeFencePluginCacheCollector(file)

    val linkMap = LinkMap.buildLinkMap(parsedTree, text)
    val map = MarkdownParserManager.FLAVOUR.createHtmlGeneratingProviders(linkMap, baseUri).toMutableMap()
    map.putAll(MarkdownParserManager.CODE_FENCE_PLUGIN_FLAVOUR.createHtmlGeneratingProviders(cacheCollector))
    if (project != null) {
      map[MarkdownElementTypes.IMAGE] = IntelliJImageGeneratingProvider(linkMap, baseUri)
    }

    val html = HtmlGenerator(text, parsedTree, map, true).generateHtml()

    MarkdownCodeFencePluginCache.getInstance().registerCacheProvider(cacheCollector)

    return html
  }
}
