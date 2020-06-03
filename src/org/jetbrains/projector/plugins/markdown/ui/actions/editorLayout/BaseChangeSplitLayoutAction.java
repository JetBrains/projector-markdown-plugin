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
package org.jetbrains.projector.plugins.markdown.ui.actions.editorLayout;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Toggleable;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.ui.actions.MarkdownActionUtil;
import org.jetbrains.projector.plugins.markdown.ui.split.SplitFileEditor;

abstract class BaseChangeSplitLayoutAction extends AnAction implements DumbAware, Toggleable {
  @Nullable
  private final SplitFileEditor.SplitEditorLayout myLayoutToSet;

  protected BaseChangeSplitLayoutAction(@Nullable SplitFileEditor.SplitEditorLayout layoutToSet) {
    myLayoutToSet = layoutToSet;
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    final SplitFileEditor splitFileEditor = MarkdownActionUtil.findSplitEditor(e);
    e.getPresentation().setEnabled(splitFileEditor != null);

    if (myLayoutToSet != null && splitFileEditor != null) {
      Toggleable.setSelected(e.getPresentation(), splitFileEditor.getCurrentEditorLayout() == myLayoutToSet);
    }
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final SplitFileEditor splitFileEditor = MarkdownActionUtil.findSplitEditor(e);

    if (splitFileEditor != null) {
      if (myLayoutToSet == null) {
        splitFileEditor.triggerLayoutChange();
      }
      else {
        splitFileEditor.triggerLayoutChange(myLayoutToSet, true);
        Toggleable.setSelected(e.getPresentation(), true);
      }
    }
  }
}
