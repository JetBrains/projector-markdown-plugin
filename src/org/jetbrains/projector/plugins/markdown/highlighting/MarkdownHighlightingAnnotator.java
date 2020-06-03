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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;

public class MarkdownHighlightingAnnotator implements Annotator {

  private static final SyntaxHighlighter SYNTAX_HIGHLIGHTER = new MarkdownSyntaxHighlighter();

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    final IElementType type = element.getNode().getElementType();

    if (type == MarkdownTokenTypes.EMPH) {
      final PsiElement parent = element.getParent();
      if (parent == null) {
        return;
      }

      final IElementType parentType = parent.getNode().getElementType();
      if (parentType == MarkdownElementTypes.EMPH || parentType == MarkdownElementTypes.STRONG) {
        final Annotation annotation = holder.createInfoAnnotation(element, null);
        annotation.setTextAttributes(parentType == MarkdownElementTypes.EMPH
                                     ? MarkdownHighlighterColors.ITALIC_MARKER_ATTR_KEY
                                     : MarkdownHighlighterColors.BOLD_MARKER_ATTR_KEY);
      }
      return;
    }

    if (element instanceof LeafPsiElement) {
      return;
    }

    final TextAttributesKey[] tokenHighlights = SYNTAX_HIGHLIGHTER.getTokenHighlights(type);

    if (tokenHighlights.length > 0 && !MarkdownHighlighterColors.TEXT_ATTR_KEY.equals(tokenHighlights[0])) {
      final Annotation annotation = holder.createInfoAnnotation(element, null);
      annotation.setTextAttributes(tokenHighlights[0]);
    }
  }
}
