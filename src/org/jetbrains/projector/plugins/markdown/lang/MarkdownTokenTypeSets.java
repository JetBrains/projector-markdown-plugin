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

import com.intellij.psi.tree.TokenSet;

public interface MarkdownTokenTypeSets extends MarkdownElementTypes {
  TokenSet HEADER_MARKERS = TokenSet.create(
    MarkdownTokenTypes.ATX_HEADER,
    MarkdownTokenTypes.SETEXT_1,
    MarkdownTokenTypes.SETEXT_2);
  TokenSet HEADER_LEVEL_1_SET = TokenSet.create(ATX_1, SETEXT_1);
  TokenSet HEADER_LEVEL_2_SET = TokenSet.create(ATX_2, SETEXT_2);
  TokenSet HEADER_LEVEL_3_SET = TokenSet.create(ATX_3);
  TokenSet HEADER_LEVEL_4_SET = TokenSet.create(ATX_4);
  TokenSet HEADER_LEVEL_5_SET = TokenSet.create(ATX_5);
  TokenSet HEADER_LEVEL_6_SET = TokenSet.create(ATX_6);
  TokenSet HEADERS = TokenSet.orSet(HEADER_LEVEL_1_SET,
                                    HEADER_LEVEL_2_SET,
                                    HEADER_LEVEL_3_SET,
                                    HEADER_LEVEL_4_SET,
                                    HEADER_LEVEL_5_SET,
                                    HEADER_LEVEL_6_SET);

  TokenSet REFERENCE_LINK_SET = TokenSet.create(FULL_REFERENCE_LINK, SHORT_REFERENCE_LINK);

  TokenSet CODE_FENCE_ITEMS = TokenSet.create(MarkdownTokenTypes.CODE_FENCE_CONTENT,
                                              MarkdownTokenTypes.CODE_FENCE_START,
                                              MarkdownTokenTypes.CODE_FENCE_END,
                                              MarkdownTokenTypes.FENCE_LANG);

  TokenSet LIST_MARKERS = TokenSet.create(MarkdownTokenTypes.LIST_BULLET, MarkdownTokenTypes.LIST_NUMBER);

  TokenSet LISTS = TokenSet.create(ORDERED_LIST, UNORDERED_LIST);

  TokenSet INLINE_HOLDING_ELEMENT_TYPES = TokenSet.orSet(HEADERS, TokenSet.create(PARAGRAPH,
                                                                                  MarkdownTokenTypes.ATX_CONTENT,
                                                                                  MarkdownTokenTypes.SETEXT_CONTENT,
                                                                                  LINK_TEXT));


  TokenSet AUTO_LINKS = TokenSet.create(AUTOLINK,
                                        MarkdownTokenTypes.GFM_AUTOLINK,
                                        MarkdownTokenTypes.EMAIL_AUTOLINK);

  TokenSet LINKS = TokenSet.orSet(AUTO_LINKS, TokenSet.create(INLINE_LINK));

  TokenSet INLINE_HOLDING_ELEMENT_PARENTS_TYPES =
    TokenSet.create(MarkdownTokenTypes.ATX_HEADER,
                    MarkdownTokenTypes.SETEXT_1,
                    MarkdownTokenTypes.SETEXT_2);
}
