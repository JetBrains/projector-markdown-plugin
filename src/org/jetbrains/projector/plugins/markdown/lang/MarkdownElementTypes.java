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

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import org.intellij.markdown.flavours.gfm.GFMElementTypes;
import org.intellij.markdown.flavours.gfm.GFMTokenTypes;

public interface MarkdownElementTypes {
  IFileElementType MARKDOWN_FILE_ELEMENT_TYPE = new IStubFileElementType("Markdown file", MarkdownLanguage.INSTANCE);

  IElementType MARKDOWN_FILE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.MARKDOWN_FILE);

  IElementType UNORDERED_LIST = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST);

  IElementType ORDERED_LIST = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST);

  IElementType LIST_ITEM = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LIST_ITEM);

  IElementType BLOCK_QUOTE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.BLOCK_QUOTE);

  IElementType CODE_FENCE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.CODE_FENCE);

  IElementType CODE_BLOCK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.CODE_BLOCK);

  IElementType CODE_SPAN = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.CODE_SPAN);

  IElementType PARAGRAPH = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.PARAGRAPH);

  IElementType EMPH = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.EMPH);

  IElementType STRONG = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.STRONG);

  IElementType STRIKETHROUGH = MarkdownElementType.platformType(GFMElementTypes.STRIKETHROUGH);

  IElementType LINK_DEFINITION = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LINK_DEFINITION);
  IElementType LINK_LABEL = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LINK_LABEL);
  IElementType LINK_DESTINATION = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LINK_DESTINATION);
  IElementType LINK_TITLE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LINK_TITLE);
  IElementType LINK_TEXT = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.LINK_TEXT);
  IElementType INLINE_LINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.INLINE_LINK);
  IElementType FULL_REFERENCE_LINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.FULL_REFERENCE_LINK);
  IElementType SHORT_REFERENCE_LINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.SHORT_REFERENCE_LINK);
  IElementType IMAGE = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.IMAGE);

  IElementType HTML_BLOCK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.HTML_BLOCK);

  IElementType MARKDOWN_OUTER_BLOCK = new IElementType("MARKDOWN_OUTER_BLOCK", MarkdownLanguage.INSTANCE);

  TemplateDataElementType MARKDOWN_TEMPLATE_DATA =
    new TemplateDataElementType("MARKDOWN_TEMPLATE_DATA", MarkdownLanguage.INSTANCE, MarkdownTokenTypes.HTML_BLOCK_CONTENT,
                                MARKDOWN_OUTER_BLOCK);

  IElementType AUTOLINK = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.AUTOLINK);

  IElementType TABLE = MarkdownElementType.platformType(GFMElementTypes.TABLE);
  IElementType TABLE_ROW = MarkdownElementType.platformType(GFMElementTypes.ROW);
  IElementType TABLE_HEADER = MarkdownElementType.platformType(GFMElementTypes.HEADER);
  IElementType TABLE_CELL = MarkdownElementType.platformType(GFMTokenTypes.CELL);

  IElementType SETEXT_1 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.SETEXT_1);
  IElementType SETEXT_2 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.SETEXT_2);

  IElementType ATX_1 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_1);
  IElementType ATX_2 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_2);
  IElementType ATX_3 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_3);
  IElementType ATX_4 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_4);
  IElementType ATX_5 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_5);
  IElementType ATX_6 = MarkdownElementType.platformType(org.intellij.markdown.MarkdownElementTypes.ATX_6);

  org.intellij.markdown.MarkdownElementType COMMENT = new org.intellij.markdown.MarkdownElementType("COMMENT", true);

  IElementType LINK_COMMENT = MarkdownElementType.platformType(COMMENT);
}
