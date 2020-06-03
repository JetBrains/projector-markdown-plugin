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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.jetbrains.projector.plugins.markdown.structureView.MarkdownBasePresentation;

public class MarkdownLinkDefinitionImpl extends ASTWrapperPsiElement implements MarkdownPsiElement {
  public MarkdownLinkDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  public PsiElement getLinkLabel() {
    final PsiElement label = findChildByType(MarkdownElementTypes.LINK_LABEL);
    if (label == null) {
      throw new IllegalStateException("Probably parsing failed. Should have a label");
    }
    return label;
  }

  @NotNull
  public PsiElement getLinkDestination() {
    final PsiElement destination = findChildByType(MarkdownElementTypes.LINK_DESTINATION);
    if (destination == null) {
      throw new IllegalStateException("Probably parsing failed. Should have a destination");
    }
    return destination;
  }

  @Nullable
  public PsiElement getLinkTitle() {
    return findChildByType(MarkdownElementTypes.LINK_TITLE);
  }

  @Override
  public ItemPresentation getPresentation() {
    return new MarkdownBasePresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        if (!isValid()) {
          return null;
        }

        return "Def: " + getLinkLabel().getText() + " → " + getLinkDestination().getText();
      }

      @Nullable
      @Override
      public String getLocationString() {
        if (!isValid()) {
          return null;
        }

        final PsiElement linkTitle = getLinkTitle();
        if (linkTitle == null) {
          return null;
        }
        else {
          return linkTitle.getText();
        }
      }
    };
  }
}
