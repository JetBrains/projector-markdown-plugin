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
package org.jetbrains.projector.plugins.markdown.ui.actions.scrolling

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware
import org.jetbrains.projector.plugins.markdown.ui.actions.MarkdownActionUtil
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownSplitEditor
import org.jetbrains.projector.plugins.markdown.ui.split.SplitFileEditor

class AutoScrollAction : ToggleAction(), DumbAware {
  override fun isSelected(e: AnActionEvent): Boolean {
    val splitFileEditor = MarkdownActionUtil.findSplitEditor(e)
    if (splitFileEditor !is MarkdownSplitEditor) return false

    e.presentation.isEnabled = splitFileEditor.currentEditorLayout == SplitFileEditor.SplitEditorLayout.SPLIT

    return splitFileEditor.isAutoScrollPreview
  }

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.isEnabled = MarkdownActionUtil.findSplitEditor(e) is MarkdownSplitEditor
  }

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    val splitFileEditor = MarkdownActionUtil.findSplitEditor(e)
    if (splitFileEditor !is MarkdownSplitEditor) return

    splitFileEditor.isAutoScrollPreview = state
  }
}
