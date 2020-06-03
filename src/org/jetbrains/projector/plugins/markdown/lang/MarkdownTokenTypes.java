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
package org.jetbrains.projector.plugins.markdown.lang;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.intellij.markdown.flavours.gfm.GFMTokenTypes;

public interface MarkdownTokenTypes extends TokenType {

  IElementType TEXT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.TEXT);

  IElementType CODE_LINE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.CODE_LINE);

  IElementType ATX_CONTENT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.ATX_CONTENT);

  IElementType SETEXT_CONTENT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.SETEXT_CONTENT);

  IElementType BLOCK_QUOTE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.BLOCK_QUOTE);

  IElementType HTML_BLOCK_CONTENT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.HTML_BLOCK_CONTENT);

  IElementType SINGLE_QUOTE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.SINGLE_QUOTE);
  IElementType DOUBLE_QUOTE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.DOUBLE_QUOTE);
  IElementType LPAREN = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LPAREN);
  IElementType RPAREN = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.RPAREN);
  IElementType LBRACKET = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LBRACKET);
  IElementType RBRACKET = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.RBRACKET);
  IElementType LT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LT);
  IElementType GT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.GT);

  IElementType COLON = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.COLON);
  IElementType EXCLAMATION_MARK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.EXCLAMATION_MARK);


  IElementType HARD_LINE_BREAK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.HARD_LINE_BREAK);
  IElementType EOL = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.EOL);

  IElementType LINK_ID = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LINK_ID);
  IElementType ATX_HEADER = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.ATX_HEADER);
  IElementType EMPH = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.EMPH);
  IElementType TILDE = MarkdownElementType.platformType(GFMTokenTypes.TILDE);

  IElementType BACKTICK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.BACKTICK);
  IElementType ESCAPED_BACKTICKS = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.ESCAPED_BACKTICKS);

  IElementType LIST_BULLET = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LIST_BULLET);
  IElementType URL = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.URL);
  IElementType HORIZONTAL_RULE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.HORIZONTAL_RULE);
  IElementType TABLE_SEPARATOR = MarkdownElementType.platformType(GFMTokenTypes.TABLE_SEPARATOR);
  IElementType SETEXT_1 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.SETEXT_1);
  IElementType SETEXT_2 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.SETEXT_2);
  IElementType LIST_NUMBER = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LIST_NUMBER);
  IElementType FENCE_LANG = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.FENCE_LANG);
  IElementType CODE_FENCE_START = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.CODE_FENCE_START);
  IElementType CODE_FENCE_END = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.CODE_FENCE_END);
  IElementType CODE_FENCE_CONTENT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.CODE_FENCE_CONTENT);
  IElementType LINK_TITLE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.LINK_TITLE);

  IElementType GFM_AUTOLINK = MarkdownElementType.platformType(GFMTokenTypes.GFM_AUTOLINK);
  IElementType AUTOLINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.AUTOLINK);
  IElementType EMAIL_AUTOLINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.EMAIL_AUTOLINK);
  IElementType HTML_TAG = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.HTML_TAG);

  IElementType CHECK_BOX = MarkdownElementType.platformType(GFMTokenTypes.CHECK_BOX);

  IElementType BAD_CHARACTER = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.BAD_CHARACTER);
  IElementType WHITE_SPACE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownTokenTypes.WHITE_SPACE);
}
