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
package org.jetbrains.projector.plugins.markdown.braces;

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownLanguage;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;

public class MarkdownBraceMatcher extends PairedBraceMatcherAdapter {

  public MarkdownBraceMatcher() {
    super(new MyPairedBraceMatcher(), MarkdownLanguage.INSTANCE);
  }

  private static class MyPairedBraceMatcher implements PairedBraceMatcher {

    @Override
    public BracePair[] getPairs() {
      return new BracePair[]{
        new BracePair(MarkdownTokenTypes.LPAREN, MarkdownTokenTypes.RPAREN, false),
        new BracePair(MarkdownTokenTypes.LBRACKET, MarkdownTokenTypes.RBRACKET, false),
        new BracePair(MarkdownTokenTypes.LT, MarkdownTokenTypes.GT, false),
        new BracePair(MarkdownTokenTypes.CODE_FENCE_START, MarkdownTokenTypes.CODE_FENCE_END, true)
      };
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, IElementType type) {
      return type == null ||
             type == MarkdownTokenTypes.WHITE_SPACE ||
             type == MarkdownTokenTypes.EOL ||
             type == MarkdownTokenTypes.RPAREN ||
             type == MarkdownTokenTypes.RBRACKET ||
             type == MarkdownTokenTypes.GT;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
      return openingBraceOffset;
    }
  }
}
