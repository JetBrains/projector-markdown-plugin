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

import java.awt.Dimension
import java.awt.Point
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

object ProjectorMarkdownHtmlPanelUpdater {

  private val idToPanelLock = ReentrantReadWriteLock()

  private val idToPanel = mutableMapOf<Int, ProjectorMarkdownHtmlPanel>()

  fun put(panel: ProjectorMarkdownHtmlPanel) {
    idToPanelLock.write {
      idToPanel[panel.id] = panel
    }
  }

  fun updateAll() {
    idToPanelLock.read {
      idToPanel.keys.forEach {
        show(it)
        resize(it)
        move(it)
        placeToWindow(it)
        setHtml(it)
        setCss(it)
        scroll(it)
      }
    }
  }

  fun show(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.showCallback?.accept(id, panel.shown)
  }

  fun resize(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.resizeCallback?.accept(id, Dimension(panel.width, panel.height))
  }

  fun move(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.moveCallback?.accept(id, Point(panel.x, panel.y))
  }

  fun dispose(id: Int) {
    idToPanelLock.write { idToPanel.remove(id) }

    ProjectorMarkdownHtmlPanelProvider.disposeCallback?.accept(id)
  }

  fun placeToWindow(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.placeToWindowCallback?.accept(id, panel.rootComponent)
  }

  fun setHtml(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.setHtmlCallback?.accept(id, panel.lastChangedHtml)
  }

  fun setCss(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.setCssCallback?.accept(id, panel.lastCssString)
  }

  fun scroll(id: Int) {
    val panel = idToPanelLock.read { idToPanel[id] ?: return }

    ProjectorMarkdownHtmlPanelProvider.scrollCallback?.accept(id, panel.lastScrollOffset)
  }
}
