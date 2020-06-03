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
package org.jetbrains.projector.plugins.markdown.highlighting;

import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownFile;

public class MarkdownIndexPatternBuilder implements IndexPatternBuilder {
  public static final TokenSet COMMENT_TOKEN_SET = TokenSet.create(MarkdownElementTypes.LINK_COMMENT);

  @Nullable
  @Override
  public Lexer getIndexingLexer(@NotNull PsiFile file) {
    if (!(file instanceof MarkdownFile)) {
      return null;
    }

    try {
      LayeredLexer.ourDisableLayersFlag.set(Boolean.TRUE);
      return ((MarkdownFile)file).getParserDefinition().createLexer(file.getProject());
    }
    finally {
      LayeredLexer.ourDisableLayersFlag.set(null);
    }
  }

  @Nullable
  @Override
  public TokenSet getCommentTokenSet(@NotNull PsiFile file) {
    return file instanceof MarkdownFile ? COMMENT_TOKEN_SET : null;
  }

  @Override
  public int getCommentStartDelta(IElementType tokenType) {
    return 1;
  }

  @Override
  public int getCommentEndDelta(IElementType tokenType) {
    return 1;
  }
}
