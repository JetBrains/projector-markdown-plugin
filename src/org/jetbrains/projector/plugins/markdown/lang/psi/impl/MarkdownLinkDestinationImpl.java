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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownElementVisitor;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;

public class MarkdownLinkDestinationImpl extends ASTWrapperPsiElement implements MarkdownPsiElement {
  public MarkdownLinkDestinationImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownElementVisitor) {
      ((MarkdownElementVisitor)visitor).visitLinkDestination(this);
      return;
    }

    super.accept(visitor);
  }

  @Override
  public PsiReference[] getReferences() {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this);
  }

  public static class Manipulator extends AbstractElementManipulator<MarkdownLinkDestinationImpl> {

    @Override
    public MarkdownLinkDestinationImpl handleContentChange(@NotNull MarkdownLinkDestinationImpl element,
                                                           @NotNull TextRange range,
                                                           String newContent) throws IncorrectOperationException {
      final PsiElement child = element.getFirstChild();
      if (child instanceof LeafPsiElement) {
        ((LeafPsiElement)child).replaceWithText(range.replace(child.getText(), newContent));
      }
      else {
        throw new IncorrectOperationException("Bad child");
      }

      return element;
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull MarkdownLinkDestinationImpl element) {
      final String text = element.getText();
      if (text.startsWith("<") && text.endsWith(">")) {
        return TextRange.create(1, text.length() - 1);
      }
      else {
        return TextRange.allOf(text);
      }
    }
  }
}
