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

import com.intellij.codeInsight.editorActions.QuoteHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;

public class MarkdownQuoteHandler implements QuoteHandler {
  private final static TokenSet QUOTE_TYPES = TokenSet.create(MarkdownTokenTypes.EMPH,
                                                              //MarkdownTokenTypes.TILDE,
                                                              MarkdownTokenTypes.BACKTICK,
                                                              MarkdownTokenTypes.SINGLE_QUOTE,
                                                              MarkdownTokenTypes.DOUBLE_QUOTE,
                                                              MarkdownTokenTypes.CODE_FENCE_START);

  @Override
  public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
    final CharSequence charsSequence = iterator.getDocument().getCharsSequence();
    final TextRange current = getRangeOfThisType(charsSequence, offset);

    final boolean isBacktick = charsSequence.charAt(offset) == '`';
    final boolean seekPrev = isBacktick ||
                             (current.getStartOffset() - 1 >= 0 &&
                              !Character.isWhitespace(charsSequence.charAt(current.getStartOffset() - 1)));

    if (seekPrev) {
      final int prev = locateNextPosition(charsSequence, charsSequence.charAt(offset), current.getStartOffset() - 1, -1);
      if (prev != -1) {
        return getRangeOfThisType(charsSequence, prev).getLength() <= current.getLength();
      }
    }
    return current.getLength() % 2 == 0 && (!isBacktick || offset > (current.getStartOffset() + current.getEndOffset()) / 2);
  }

  @Override
  public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
    final IElementType tokenType = iterator.getTokenType();

    if (!QUOTE_TYPES.contains(tokenType)) {
      return false;
    }

    final CharSequence chars = iterator.getDocument().getCharsSequence();

    final boolean isBacktick = chars.charAt(offset) == '`';
    if (isBacktick && isClosingQuote(iterator, offset)) {
      return false;
    }

    return getRangeOfThisType(chars, offset).getLength() != 1 ||
           ((offset <= 0 || Character.isWhitespace(chars.charAt(offset - 1)))
            && (offset + 1 >= chars.length() || Character.isWhitespace(chars.charAt(offset + 1))));
  }

  @Override
  public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
    final CharSequence charsSequence = editor.getDocument().getCharsSequence();
    final TextRange current = getRangeOfThisType(charsSequence, offset);

    final int next = locateNextPosition(charsSequence, charsSequence.charAt(offset), current.getEndOffset(), +1);
    return next == -1 || getRangeOfThisType(charsSequence, next).getLength() < current.getLength();
  }

  @Override
  public boolean isInsideLiteral(HighlighterIterator iterator) {
    return false;
  }

  private static TextRange getRangeOfThisType(@NotNull CharSequence charSequence, int offset) {
    final int length = charSequence.length();
    final char c = charSequence.charAt(offset);

    int l = offset, r = offset;
    while (l - 1 >= 0 && charSequence.charAt(l - 1) == c) {
      l--;
    }
    while (r + 1 < length && charSequence.charAt(r + 1) == c) {
      r++;
    }
    return TextRange.create(l, r + 1);
  }

  private static int locateNextPosition(@NotNull CharSequence haystack, char needle, int from, int dx) {
    while (from >= 0 && from < haystack.length()) {
      final char currentChar = haystack.charAt(from);
      if (currentChar == needle) {
        return from;
      }
      else if (currentChar == '\n') {
        return -1;
      }

      from += dx;
    }
    return -1;
  }
}
