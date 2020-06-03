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
package org.jetbrains.projector.plugins.markdown.lang.parser;

import com.intellij.openapi.util.Key;
import org.intellij.markdown.MarkdownElementTypes;
import org.intellij.markdown.ast.ASTNode;
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor;
import org.intellij.markdown.parser.MarkdownParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.extensions.CodeFencePluginFlavourDescriptor;

public class MarkdownParserManager {
  public static final Key<MarkdownFlavourDescriptor> FLAVOUR_DESCRIPTION = Key.create("Markdown.Flavour");

  public static final GFMCommentAwareFlavourDescriptor FLAVOUR = new GFMCommentAwareFlavourDescriptor();
  public static final CodeFencePluginFlavourDescriptor CODE_FENCE_PLUGIN_FLAVOUR = new CodeFencePluginFlavourDescriptor();

  private static final ThreadLocal<ParsingInfo> ourLastParsingResult = new ThreadLocal<>();

  public static ASTNode parseContent(@NotNull CharSequence buffer) {
    return parseContent(buffer, FLAVOUR);
  }

  public static ASTNode parseContent(@NotNull CharSequence buffer, @NotNull MarkdownFlavourDescriptor flavour) {
    final ParsingInfo info = ourLastParsingResult.get();
    if (info != null && info.myBufferHash == buffer.hashCode() && info.myBuffer.equals(buffer)) {
      return info.myParseResult;
    }

    final ASTNode parseResult = new MarkdownParser(flavour)
      .parse(MarkdownElementTypes.MARKDOWN_FILE, buffer.toString(), false);
    ourLastParsingResult.set(new ParsingInfo(buffer, parseResult));
    return parseResult;
  }

  private static class ParsingInfo {
    @NotNull final CharSequence myBuffer;
    final int myBufferHash;
    @NotNull final ASTNode myParseResult;

    ParsingInfo(@NotNull CharSequence buffer, @NotNull ASTNode parseResult) {
      myBuffer = buffer;
      myBufferHash = myBuffer.hashCode();
      myParseResult = parseResult;
    }
  }
}
