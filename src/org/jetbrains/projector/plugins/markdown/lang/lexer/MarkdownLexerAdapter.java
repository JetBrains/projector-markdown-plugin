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
package org.jetbrains.projector.plugins.markdown.lang.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.intellij.markdown.lexer.MarkdownLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementType;
import org.jetbrains.projector.plugins.markdown.lang.parser.MarkdownParserManager;

public class MarkdownLexerAdapter extends LexerBase {
  @NotNull
  private final MarkdownLexer delegateLexer = MarkdownParserManager.FLAVOUR.createInlinesLexer();

  private int endOffset;

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    this.endOffset = endOffset;
    delegateLexer.start(buffer, startOffset, endOffset);
  }

  @Override
  public int getState() {
    return 1;
  }

  @Nullable
  @Override
  public IElementType getTokenType() {
    return MarkdownElementType.platformType(delegateLexer.getType());
  }

  @Override
  public int getTokenStart() {
    return delegateLexer.getTokenStart();
  }

  @Override
  public int getTokenEnd() {
    return delegateLexer.getTokenEnd();
  }

  @Override
  public void advance() {
    delegateLexer.advance();
  }

  @NotNull
  @Override
  public CharSequence getBufferSequence() {
    return delegateLexer.getOriginalText();
  }

  @Override
  public int getBufferEnd() {
    return endOffset;
  }
}
