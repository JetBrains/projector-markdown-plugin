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
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.MarkdownBundle;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MarkdownColorSettingsPage implements ColorSettingsPage {

  private static final AttributesDescriptor[] ATTRIBUTE_DESCRIPTORS = AttributeDescriptorsHolder.INSTANCE.get();

  @Override
  @NotNull
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    final Map<String, TextAttributesKey> result = new HashMap<>();

    result.put("hh1", MarkdownHighlighterColors.HEADER_LEVEL_1_ATTR_KEY);
    result.put("hh2", MarkdownHighlighterColors.HEADER_LEVEL_2_ATTR_KEY);
    result.put("hh3", MarkdownHighlighterColors.HEADER_LEVEL_3_ATTR_KEY);
    result.put("hh4", MarkdownHighlighterColors.HEADER_LEVEL_4_ATTR_KEY);
    result.put("hh5", MarkdownHighlighterColors.HEADER_LEVEL_5_ATTR_KEY);
    result.put("hh6", MarkdownHighlighterColors.HEADER_LEVEL_6_ATTR_KEY);

    result.put("bold", MarkdownHighlighterColors.BOLD_ATTR_KEY);
    result.put("boldm", MarkdownHighlighterColors.BOLD_MARKER_ATTR_KEY);
    result.put("italic", MarkdownHighlighterColors.ITALIC_ATTR_KEY);
    result.put("italicm", MarkdownHighlighterColors.ITALIC_MARKER_ATTR_KEY);
    result.put("strike", MarkdownHighlighterColors.STRIKE_THROUGH_ATTR_KEY);

    result.put("alink", MarkdownHighlighterColors.AUTO_LINK_ATTR_KEY);
    result.put("link_def", MarkdownHighlighterColors.LINK_DEFINITION_ATTR_KEY);
    result.put("link_text", MarkdownHighlighterColors.LINK_TEXT_ATTR_KEY);
    result.put("link_label", MarkdownHighlighterColors.LINK_LABEL_ATTR_KEY);
    result.put("link_dest", MarkdownHighlighterColors.LINK_DESTINATION_ATTR_KEY);
    result.put("link_img", MarkdownHighlighterColors.IMAGE_ATTR_KEY);
    result.put("link_title", MarkdownHighlighterColors.LINK_TITLE_ATTR_KEY);

    result.put("code_span", MarkdownHighlighterColors.CODE_SPAN_ATTR_KEY);
    result.put("code_block", MarkdownHighlighterColors.CODE_BLOCK_ATTR_KEY);
    result.put("code_fence", MarkdownHighlighterColors.CODE_FENCE_ATTR_KEY);
    result.put("quote", MarkdownHighlighterColors.BLOCK_QUOTE_ATTR_KEY);

    result.put("ul", MarkdownHighlighterColors.UNORDERED_LIST_ATTR_KEY);
    result.put("ol", MarkdownHighlighterColors.ORDERED_LIST_ATTR_KEY);

    return result;
  }

  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRIBUTE_DESCRIPTORS;
  }

  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @Override
  @NonNls
  @NotNull
  public String getDemoText() {
    final InputStream stream = getClass().getResourceAsStream("SampleDocument.md");

    try {
      final String result = StreamUtil.readText(stream, CharsetToolkit.UTF8);
      stream.close();
      return StringUtil.convertLineSeparators(result);
    }
    catch (IOException ignored) {
    }

    return "*error loading text*";
  }

  @Override
  @NotNull
  public String getDisplayName() {
    return MarkdownBundle.message("markdown.plugin.name");
  }

  @Override
  @NotNull
  public SyntaxHighlighter getHighlighter() {
    return new MarkdownSyntaxHighlighter();
  }

  @Override
  @Nullable
  public Icon getIcon() {
    return null;
  }

  private enum AttributeDescriptorsHolder {
    INSTANCE;

    private final Map<String, TextAttributesKey> myMap = new HashMap<>();

    AttributeDescriptorsHolder() {
      put("markdown.editor.colors.text", MarkdownHighlighterColors.TEXT_ATTR_KEY);
      put("markdown.editor.colors.bold", MarkdownHighlighterColors.BOLD_ATTR_KEY);
      put("markdown.editor.colors.bold_marker", MarkdownHighlighterColors.BOLD_MARKER_ATTR_KEY);
      put("markdown.editor.colors.italic", MarkdownHighlighterColors.ITALIC_ATTR_KEY);
      put("markdown.editor.colors.italic_marker", MarkdownHighlighterColors.ITALIC_MARKER_ATTR_KEY);
      put("markdown.editor.colors.strikethrough", MarkdownHighlighterColors.STRIKE_THROUGH_ATTR_KEY);
      put("markdown.editor.colors.header_level_1", MarkdownHighlighterColors.HEADER_LEVEL_1_ATTR_KEY);
      put("markdown.editor.colors.header_level_2", MarkdownHighlighterColors.HEADER_LEVEL_2_ATTR_KEY);
      put("markdown.editor.colors.header_level_3", MarkdownHighlighterColors.HEADER_LEVEL_3_ATTR_KEY);
      put("markdown.editor.colors.header_level_4", MarkdownHighlighterColors.HEADER_LEVEL_4_ATTR_KEY);
      put("markdown.editor.colors.header_level_5", MarkdownHighlighterColors.HEADER_LEVEL_5_ATTR_KEY);
      put("markdown.editor.colors.header_level_6", MarkdownHighlighterColors.HEADER_LEVEL_6_ATTR_KEY);

      put("markdown.editor.colors.blockquote", MarkdownHighlighterColors.BLOCK_QUOTE_ATTR_KEY);

      put("markdown.editor.colors.code_span", MarkdownHighlighterColors.CODE_SPAN_ATTR_KEY);
      put("markdown.editor.colors.code_span_marker", MarkdownHighlighterColors.CODE_SPAN_MARKER_ATTR_KEY);
      put("markdown.editor.colors.code_block", MarkdownHighlighterColors.CODE_BLOCK_ATTR_KEY);
      put("markdown.editor.colors.code_fence", MarkdownHighlighterColors.CODE_FENCE_ATTR_KEY);

      put("markdown.editor.colors.hrule", MarkdownHighlighterColors.HRULE_ATTR_KEY);
      put("markdown.editor.colors.table_separator", MarkdownHighlighterColors.TABLE_SEPARATOR_ATTR_KEY);
      put("markdown.editor.colors.blockquote_marker", MarkdownHighlighterColors.BLOCK_QUOTE_MARKER_ATTR_KEY);
      put("markdown.editor.colors.list_marker", MarkdownHighlighterColors.LIST_MARKER_ATTR_KEY);
      put("markdown.editor.colors.header_marker", MarkdownHighlighterColors.HEADER_MARKER_ATTR_KEY);

      put("markdown.editor.colors.auto_link", MarkdownHighlighterColors.AUTO_LINK_ATTR_KEY);
      put("markdown.editor.colors.explicit_link", MarkdownHighlighterColors.EXPLICIT_LINK_ATTR_KEY);
      put("markdown.editor.colors.reference_link", MarkdownHighlighterColors.REFERENCE_LINK_ATTR_KEY);
      put("markdown.editor.colors.image", MarkdownHighlighterColors.IMAGE_ATTR_KEY);
      put("markdown.editor.colors.link_definition", MarkdownHighlighterColors.LINK_DEFINITION_ATTR_KEY);
      put("markdown.editor.colors.link_text", MarkdownHighlighterColors.LINK_TEXT_ATTR_KEY);
      put("markdown.editor.colors.link_label", MarkdownHighlighterColors.LINK_LABEL_ATTR_KEY);
      put("markdown.editor.colors.link_destination", MarkdownHighlighterColors.LINK_DESTINATION_ATTR_KEY);
      put("markdown.editor.colors.link_title", MarkdownHighlighterColors.LINK_TITLE_ATTR_KEY);

      put("markdown.editor.colors.unordered_list", MarkdownHighlighterColors.UNORDERED_LIST_ATTR_KEY);
      put("markdown.editor.colors.ordered_list", MarkdownHighlighterColors.ORDERED_LIST_ATTR_KEY);
      put("markdown.editor.colors.list_item", MarkdownHighlighterColors.LIST_ITEM_ATTR_KEY);
      put("markdown.editor.colors.html_block", MarkdownHighlighterColors.HTML_BLOCK_ATTR_KEY);
      put("markdown.editor.colors.inline_html", MarkdownHighlighterColors.INLINE_HTML_ATTR_KEY);
    }

    public AttributesDescriptor[] get() {
      final AttributesDescriptor[] result = new AttributesDescriptor[myMap.size()];
      int i = 0;

      for (Map.Entry<String, TextAttributesKey> entry : myMap.entrySet()) {
        result[i++] = new AttributesDescriptor(MarkdownBundle.message(entry.getKey()), entry.getValue());
      }

      return result;
    }

    private void put(@NotNull String bundleKey, @NotNull TextAttributesKey attributes) {
      if (myMap.put(bundleKey, attributes) != null) {
        throw new IllegalArgumentException("Duplicated key: " + bundleKey);
      }
    }
  }
}
