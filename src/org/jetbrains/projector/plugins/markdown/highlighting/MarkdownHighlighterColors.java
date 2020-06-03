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

import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;
import static com.intellij.openapi.editor.HighlighterColors.TEXT;
import static com.intellij.openapi.editor.colors.CodeInsightColors.DEPRECATED_ATTRIBUTES;
import static com.intellij.openapi.editor.colors.CodeInsightColors.HYPERLINK_ATTRIBUTES;
import static com.intellij.openapi.editor.colors.EditorColors.INJECTED_LANGUAGE_FRAGMENT;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class MarkdownHighlighterColors {

  public static final TextAttributesKey TEXT_ATTR_KEY = createTextAttributesKey("MARKDOWN_TEXT", TEXT);
  public static final TextAttributesKey BOLD_ATTR_KEY = createTextAttributesKey("MARKDOWN_BOLD");
  public static final TextAttributesKey BOLD_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_BOLD_MARKER", KEYWORD);
  public static final TextAttributesKey ITALIC_ATTR_KEY = createTextAttributesKey("MARKDOWN_ITALIC");
  public static final TextAttributesKey ITALIC_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_ITALIC_MARKER", KEYWORD);
  public static final TextAttributesKey CODE_SPAN_ATTR_KEY = createTextAttributesKey("MARKDOWN_CODE_SPAN", BLOCK_COMMENT);
  public static final TextAttributesKey CODE_SPAN_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_CODE_SPAN_MARKER", BLOCK_COMMENT);
  public static final TextAttributesKey BLOCK_QUOTE_ATTR_KEY = createTextAttributesKey("MARKDOWN_BLOCK_QUOTE", STRING);
  public static final TextAttributesKey BLOCK_QUOTE_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_BLOCK_QUOTE_MARKER", KEYWORD);
  public static final TextAttributesKey LIST_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_LIST_MARKER", KEYWORD);
  public static final TextAttributesKey HEADER_MARKER_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_MARKER", KEYWORD);
  public static final TextAttributesKey UNORDERED_LIST_ATTR_KEY = createTextAttributesKey("MARKDOWN_UNORDERED_LIST");
  public static final TextAttributesKey ORDERED_LIST_ATTR_KEY = createTextAttributesKey("MARKDOWN_ORDERED_LIST");
  public static final TextAttributesKey HTML_BLOCK_ATTR_KEY = createTextAttributesKey("MARKDOWN_HTML_BLOCK", TEXT);
  public static final TextAttributesKey INLINE_HTML_ATTR_KEY = createTextAttributesKey("MARKDOWN_INLINE_HTML", TEXT);
  public static final TextAttributesKey HEADER_LEVEL_1_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_1", CONSTANT);
  public static final TextAttributesKey HEADER_LEVEL_2_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_2", CONSTANT);
  public static final TextAttributesKey HEADER_LEVEL_3_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_3", CONSTANT);
  public static final TextAttributesKey HEADER_LEVEL_4_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_4", CONSTANT);
  public static final TextAttributesKey HEADER_LEVEL_5_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_5", CONSTANT);
  public static final TextAttributesKey HEADER_LEVEL_6_ATTR_KEY = createTextAttributesKey("MARKDOWN_HEADER_LEVEL_6", CONSTANT);
  public static final TextAttributesKey HRULE_ATTR_KEY = createTextAttributesKey("MARKDOWN_HRULE", BLOCK_COMMENT);
  public static final TextAttributesKey CODE_BLOCK_ATTR_KEY = createTextAttributesKey("MARKDOWN_CODE_BLOCK", BLOCK_COMMENT);
  public static final TextAttributesKey CODE_FENCE_ATTR_KEY = createTextAttributesKey("MARKDOWN_CODE_FENCE", BLOCK_COMMENT);

  public static final TextAttributesKey LIST_ITEM_ATTR_KEY = createTextAttributesKey("MARKDOWN_LIST_ITEM", TEXT);
  public static final TextAttributesKey TABLE_SEPARATOR_ATTR_KEY = createTextAttributesKey("MARKDOWN_TABLE_SEPARATOR", BLOCK_COMMENT);
  public static final TextAttributesKey STRIKE_THROUGH_ATTR_KEY = createTextAttributesKey("MARKDOWN_STRIKE_THROUGH", DEPRECATED_ATTRIBUTES);

  public static final TextAttributesKey LINK_DEFINITION_ATTR_KEY = createTextAttributesKey("MARKDOWN_LINK_DEFINITION");
  public static final TextAttributesKey REFERENCE_LINK_ATTR_KEY = createTextAttributesKey("MARKDOWN_REFERENCE_LINK");
  public static final TextAttributesKey IMAGE_ATTR_KEY = createTextAttributesKey("MARKDOWN_IMAGE", INJECTED_LANGUAGE_FRAGMENT);
  public static final TextAttributesKey EXPLICIT_LINK_ATTR_KEY = createTextAttributesKey("MARKDOWN_EXPLICIT_LINK", STRING);
  public static final TextAttributesKey LINK_TEXT_ATTR_KEY = createTextAttributesKey("MARKDOWN_LINK_TEXT", HYPERLINK_ATTRIBUTES);
  public static final TextAttributesKey LINK_DESTINATION_ATTR_KEY = createTextAttributesKey("MARKDOWN_LINK_DESTINATION", STATIC_METHOD);
  public static final TextAttributesKey LINK_LABEL_ATTR_KEY = createTextAttributesKey("MARKDOWN_LINK_LABEL", KEYWORD);
  public static final TextAttributesKey LINK_TITLE_ATTR_KEY = createTextAttributesKey("MARKDOWN_LINK_TITLE", BLOCK_COMMENT);
  public static final TextAttributesKey AUTO_LINK_ATTR_KEY = createTextAttributesKey("MARKDOWN_AUTO_LINK", HYPERLINK_ATTRIBUTES);

  public static final TextAttributesKey COMMENT_ATTR_KEY = createTextAttributesKey("MARKDOWN_COMMENT", LINE_COMMENT);
}
