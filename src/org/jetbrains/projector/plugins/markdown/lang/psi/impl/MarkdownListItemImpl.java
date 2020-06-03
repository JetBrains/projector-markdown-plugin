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
package org.jetbrains.projector.plugins.markdown.lang.psi.impl;

import com.intellij.execution.process.ConsoleHighlighter;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypeSets;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;
import org.jetbrains.projector.plugins.markdown.structureView.MarkdownBasePresentation;

import javax.swing.*;

public class MarkdownListItemImpl extends MarkdownCompositePsiElementBase {
  public MarkdownListItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  public PsiElement getMarkerElement() {
    final PsiElement child = getFirstChild();
    if (child != null && MarkdownTokenTypeSets.LIST_MARKERS.contains(child.getNode().getElementType())) {
      return child;
    }
    else {
      return null;
    }
  }

  @Nullable
  public PsiElement getCheckBox() {
    final PsiElement markerElement = getMarkerElement();
    if (markerElement == null) {
      return null;
    }
    final PsiElement candidate = markerElement.getNextSibling();
    if (candidate != null && candidate.getNode().getElementType() == MarkdownTokenTypes.CHECK_BOX) {
      return candidate;
    }
    else {
      return null;
    }
  }

  @Override
  public ItemPresentation getPresentation() {
    return new MyItemPresentation();
  }

  @Override
  public String getPresentableTagName() {
    return "li";
  }

  private class MyItemPresentation extends MarkdownBasePresentation implements ColoredItemPresentation {
    @Nullable
    @Override
    public String getPresentableText() {
      if (!isValid()) {
        return null;
      }
      final PsiElement markerElement = getMarkerElement();
      if (markerElement == null) {
        return null;
      }
      return markerElement.getText();
    }

    @Nullable
    @Override
    public String getLocationString() {
      if (!isValid()) {
        return null;
      }

      if (hasTrivialChildren()) {
        final MarkdownCompositePsiElementBase element = findChildByClass(MarkdownCompositePsiElementBase.class);
        assert element != null;
        return element.shrinkTextTo(PRESENTABLE_TEXT_LENGTH);
      }
      else {
        return null;
      }
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
      return null;
    }

    @Nullable
    @Override
    public TextAttributesKey getTextAttributesKey() {
      final PsiElement checkBox = getCheckBox();
      if (checkBox == null) {
        return null;
      }
      if (checkBox.textContains('x') || checkBox.textContains('X')) {
        return ConsoleHighlighter.GREEN;
      }
      else {
        return ConsoleHighlighter.RED;
      }
    }
  }
}
