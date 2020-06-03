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
package org.jetbrains.projector.plugins.markdown.ui.preview;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.settings.MarkdownApplicationSettings;
import org.jetbrains.projector.plugins.markdown.ui.split.SplitFileEditor;

public class MarkdownSplitEditor extends SplitFileEditor<TextEditor, MarkdownPreviewFileEditor> implements TextEditor {
  private boolean myAutoScrollPreview = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().isAutoScrollPreview();
  private boolean myVerticalSplit = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();

  public MarkdownSplitEditor(@NotNull TextEditor mainEditor, @NotNull MarkdownPreviewFileEditor secondEditor) {
    super(mainEditor, secondEditor);

    MarkdownApplicationSettings.SettingsChangedListener settingsChangedListener =
      new MarkdownApplicationSettings.SettingsChangedListener() {
        @Override
        public void beforeSettingsChanged(@NotNull MarkdownApplicationSettings newSettings) {
          boolean oldAutoScrollPreview = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().isAutoScrollPreview();
          boolean oldVerticalSplit = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();

          ApplicationManager.getApplication().invokeLater(() -> {
            if (oldAutoScrollPreview == myAutoScrollPreview) {
              setAutoScrollPreview(newSettings.getMarkdownPreviewSettings().isAutoScrollPreview());
            }

            if (oldVerticalSplit == myVerticalSplit) {
              setVerticalSplit(newSettings.getMarkdownPreviewSettings().isVerticalSplit());
            }
          });
        }
      };

    ApplicationManager.getApplication().getMessageBus().connect(this)
      .subscribe(MarkdownApplicationSettings.SettingsChangedListener.TOPIC, settingsChangedListener);

    mainEditor.getEditor().getCaretModel().addCaretListener(new MyCaretListener());
  }

  @NotNull
  @Override
  public String getName() {
    return "Markdown split editor";
  }

  @NotNull
  @Override
  public Editor getEditor() {
    return getMainEditor().getEditor();
  }

  @Override
  public boolean canNavigateTo(@NotNull Navigatable navigatable) {
    return getMainEditor().canNavigateTo(navigatable);
  }

  @Override
  public void navigateTo(@NotNull Navigatable navigatable) {
    getMainEditor().navigateTo(navigatable);
  }

  public boolean isAutoScrollPreview() {
    return myAutoScrollPreview;
  }

  public void setAutoScrollPreview(boolean autoScrollPreview) {
    myAutoScrollPreview = autoScrollPreview;
  }

  public void setVerticalSplit(boolean verticalSplit) {
    myVerticalSplit = verticalSplit;
  }

  private class MyCaretListener implements CaretListener {
    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
      if (!isAutoScrollPreview()) return;

      final Editor editor = e.getEditor();
      if (editor.getCaretModel().getCaretCount() != 1) {
        return;
      }

      final int offset = editor.logicalPositionToOffset(e.getNewPosition());
      getSecondEditor().scrollToSrcOffset(offset);
    }
  }
}
