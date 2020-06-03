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
package org.jetbrains.projector.plugins.markdown.ui.actions.styling;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;

public class ToggleCodeSpanAction extends BaseToggleStateAction {
  @NotNull
  @Override
  protected String getBoundString(@NotNull CharSequence text, int selectionStart, int selectionEnd) {
    int maxBacktickSequenceSeen = 0;
    int curBacktickSequence = 0;
    for (int i = selectionStart; i < selectionEnd; ++i) {
      if (text.charAt(i) != '`') {
        curBacktickSequence = 0;
      }
      else {
        curBacktickSequence++;
        maxBacktickSequenceSeen = Math.max(maxBacktickSequenceSeen, curBacktickSequence);
      }
    }

    return StringUtil.repeat("`", maxBacktickSequenceSeen + 1);
  }

  @Nullable
  @Override
  protected String getExistingBoundString(@NotNull CharSequence text, int startOffset) {
    int to = startOffset;
    while (to < text.length() && text.charAt(to) == '`') {
      to++;
    }

    return text.subSequence(startOffset, to).toString();
  }

  @Override
  protected boolean shouldMoveToWordBounds() {
    return false;
  }

  @NotNull
  @Override
  protected IElementType getTargetNodeType() {
    return MarkdownElementTypes.CODE_SPAN;
  }
}
