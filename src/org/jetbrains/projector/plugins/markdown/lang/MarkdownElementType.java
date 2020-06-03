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

import com.intellij.psi.tree.IElementType;
import org.intellij.markdown.MarkdownElementTypes;
import org.intellij.markdown.MarkdownTokenTypes;
import org.intellij.markdown.flavours.gfm.GFMTokenTypes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.stubs.impl.MarkdownHeaderStubElementType;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class MarkdownElementType extends IElementType {

  @NotNull
  private static final Map<org.intellij.markdown.IElementType, IElementType> markdownToPlatformTypeMap =
    new HashMap<>();
  @NotNull
  private static final Map<IElementType, org.intellij.markdown.IElementType> platformToMarkdownTypeMap =
    new HashMap<>();

  public MarkdownElementType(@NotNull @NonNls String debugName) {
    super(debugName, MarkdownLanguage.INSTANCE);
  }

  @Override
  @SuppressWarnings({"HardCodedStringLiteral"})
  public String toString() {
    return MessageFormat.format("Markdown:{0}", super.toString());
  }

  @Contract("null -> null; !null -> !null")
  public synchronized static IElementType platformType(@Nullable org.intellij.markdown.IElementType markdownType) {
    if (markdownType == null) {
      return null;
    }

    if (markdownToPlatformTypeMap.containsKey(markdownType)) {
      return markdownToPlatformTypeMap.get(markdownType);
    }

    final IElementType result;
    if (markdownType == MarkdownElementTypes.PARAGRAPH
        || markdownType == MarkdownTokenTypes.ATX_CONTENT
        || markdownType == MarkdownTokenTypes.SETEXT_CONTENT
        || markdownType == GFMTokenTypes.CELL) {
      result = new MarkdownLazyElementType(markdownType.toString());
    }
    else {
      result = isHeaderElementType(markdownType)
               ? new MarkdownHeaderStubElementType(markdownType.toString())
               : new MarkdownElementType(markdownType.toString());
    }
    markdownToPlatformTypeMap.put(markdownType, result);
    platformToMarkdownTypeMap.put(result, markdownType);
    return result;
  }

  private static boolean isHeaderElementType(@NotNull org.intellij.markdown.IElementType markdownType) {
    return markdownType == MarkdownElementTypes.ATX_1 ||
           markdownType == MarkdownElementTypes.ATX_2 ||
           markdownType == MarkdownElementTypes.ATX_3 ||
           markdownType == MarkdownElementTypes.ATX_4 ||
           markdownType == MarkdownElementTypes.ATX_5 ||
           markdownType == MarkdownElementTypes.ATX_6 ||
           markdownType == MarkdownElementTypes.SETEXT_1 ||
           markdownType == MarkdownElementTypes.SETEXT_2;
  }

  @Contract("!null -> !null")
  public synchronized static org.intellij.markdown.IElementType markdownType(@Nullable IElementType platformType) {
    if (platformType == null) {
      return null;
    }
    return platformToMarkdownTypeMap.get(platformType);
  }
}
