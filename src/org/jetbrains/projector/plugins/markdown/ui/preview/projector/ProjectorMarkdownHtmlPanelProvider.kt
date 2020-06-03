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
package org.jetbrains.projector.plugins.markdown.ui.preview.projector

import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownHtmlPanel
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider
import java.awt.Component
import java.awt.Dimension
import java.awt.Point
import java.util.function.BiConsumer
import java.util.function.Consumer

class ProjectorMarkdownHtmlPanelProvider : MarkdownHtmlPanelProvider() {

  override fun isAvailable(): AvailabilityInfo = AvailabilityInfo.AVAILABLE

  override fun getProviderInfo(): ProviderInfo = PROVIDER_INFO

  override fun createHtmlPanel(): MarkdownHtmlPanel = ProjectorMarkdownHtmlPanel().also {
    ProjectorMarkdownHtmlPanelUpdater.put(it)
  }

  companion object {

    var showCallback: BiConsumer<Int, Boolean>? = null
      @JvmStatic
      set

    var resizeCallback: BiConsumer<Int, Dimension>? = null
      @JvmStatic
      set

    var moveCallback: BiConsumer<Int, Point>? = null
      @JvmStatic
      set

    var disposeCallback: Consumer<Int>? = null
      @JvmStatic
      set

    var placeToWindowCallback: BiConsumer<Int, Component?>? = null
      @JvmStatic
      set

    var setHtmlCallback: BiConsumer<Int, String>? = null
      @JvmStatic
      set

    var setCssCallback: BiConsumer<Int, String>? = null
      @JvmStatic
      set

    var scrollCallback: BiConsumer<Int, Int>? = null
      @JvmStatic
      set

    var browseUriCallback: Consumer<String>? = null
      @JvmStatic
      set

    @Suppress("unused")  // it's used via reflection
    @JvmStatic
    fun updateAll() {
      ProjectorMarkdownHtmlPanelUpdater.updateAll()
    }

    @Suppress("unused")  // it's used via reflection
    @JvmStatic
    fun openInExternalBrowser(link: String) {
      SafeOpener.openInExternalBrowser(link)
    }

    private val PROVIDER_INFO = ProviderInfo("Projector WebView", ProjectorMarkdownHtmlPanelProvider::class.java.name)
  }
}
