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

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;

public class ToggleItalicAction extends BaseToggleStateAction {
  @Override
  @NotNull
  protected String getBoundString(@NotNull CharSequence text, int selectionStart, int selectionEnd) {
    return isWord(text, selectionStart, selectionEnd) ? "_" : "*";
  }

  @Override
  protected boolean shouldMoveToWordBounds() {
    return true;
  }

  @Override
  @NotNull
  protected IElementType getTargetNodeType() {
    return MarkdownElementTypes.EMPH;
  }

  private static boolean isWord(@NotNull CharSequence text, int from, int to) {
    return (from == 0 || !Character.isLetterOrDigit(text.charAt(from - 1)))
           && (to == text.length() || !Character.isLetterOrDigit(text.charAt(to)));
  }
}
