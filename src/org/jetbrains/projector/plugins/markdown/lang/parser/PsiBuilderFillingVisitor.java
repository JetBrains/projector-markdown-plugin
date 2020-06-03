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

import com.intellij.lang.PsiBuilder;
import org.intellij.markdown.IElementType;
import org.intellij.markdown.MarkdownElementTypes;
import org.intellij.markdown.ast.ASTNode;
import org.intellij.markdown.ast.LeafASTNode;
import org.intellij.markdown.ast.visitors.RecursiveVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementType;

public class PsiBuilderFillingVisitor extends RecursiveVisitor {
  @NotNull
  private final PsiBuilder builder;

  public PsiBuilderFillingVisitor(@NotNull PsiBuilder builder) {
    this.builder = builder;
  }

  @Override
  public void visitNode(@NotNull ASTNode node) {
    if (node instanceof LeafASTNode) {
      /* a hack for the link reference definitions:
       * they are being parsed independent from link references and
       * the link titles and urls are tokens instead of composite elements
       */
      final IElementType type = node.getType();
      if (type != MarkdownElementTypes.LINK_LABEL && type != MarkdownElementTypes.LINK_DESTINATION) {
        return;
      }
    }

    ensureBuilderInPosition(node.getStartOffset());
    final PsiBuilder.Marker marker = builder.mark();

    super.visitNode(node);

    ensureBuilderInPosition(node.getEndOffset());
    marker.done(MarkdownElementType.platformType(node.getType()));
  }

  private void ensureBuilderInPosition(int position) {
    while (builder.getCurrentOffset() < position) {
      builder.advanceLexer();
    }

    if (builder.getCurrentOffset() != position) {
      throw new AssertionError("parsed tree and lexer are unsynchronized");
    }
  }
}
