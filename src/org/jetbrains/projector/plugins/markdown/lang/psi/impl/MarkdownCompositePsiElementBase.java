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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.jetbrains.projector.plugins.markdown.structureView.MarkdownBasePresentation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class MarkdownCompositePsiElementBase extends ASTWrapperPsiElement implements MarkdownCompositePsiElement {
  public static final int PRESENTABLE_TEXT_LENGTH = 50;

  public MarkdownCompositePsiElementBase(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  protected CharSequence getChars() {
    return getTextRange().subSequence(getContainingFile().getViewProvider().getContents());
  }

  @NotNull
  protected String shrinkTextTo(int length) {
    final CharSequence chars = getChars();
    return chars.subSequence(0, Math.min(length, chars.length())).toString();
  }

  @NotNull
  @Override
  public List<MarkdownPsiElement> getCompositeChildren() {
    return Arrays.asList(findChildrenByClass(MarkdownPsiElement.class));
  }

  /**
   * @return {@code true} if there is more than one composite child
   * OR there is one child which is not a paragraph, {@code false} otherwise.
   */
  public boolean hasTrivialChildren() {
    final Collection<MarkdownPsiElement> children = getCompositeChildren();
    if (children.size() != 1) {
      return false;
    }
    return children.iterator().next() instanceof MarkdownParagraphImpl;
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
        return getPresentableTagName();
      }

      @Nullable
      @Override
      public String getLocationString() {
        if (!isValid()) {
          return null;
        }
        if (getCompositeChildren().size() == 0) {
          return shrinkTextTo(PRESENTABLE_TEXT_LENGTH);
        }
        else {
          return null;
        }
      }
    };
  }
}
