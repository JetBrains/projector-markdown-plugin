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

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypeSets;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownRecursiveElementVisitor;
import org.jetbrains.projector.plugins.markdown.lang.stubs.MarkdownStubBasedPsiElementBase;
import org.jetbrains.projector.plugins.markdown.lang.stubs.MarkdownStubElement;
import org.jetbrains.projector.plugins.markdown.lang.stubs.impl.MarkdownHeaderStubElement;
import org.jetbrains.projector.plugins.markdown.lang.stubs.impl.MarkdownHeaderStubElementType;
import org.jetbrains.projector.plugins.markdown.structureView.MarkdownStructureColors;

import javax.swing.*;

public class MarkdownHeaderImpl extends MarkdownStubBasedPsiElementBase<MarkdownStubElement> {
  public MarkdownHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public MarkdownHeaderImpl(MarkdownHeaderStubElement stub, MarkdownHeaderStubElementType type) {
    super(stub, type);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownRecursiveElementVisitor) {
      ((MarkdownRecursiveElementVisitor)visitor).visitHeader(this);
      return;
    }

    super.accept(visitor);
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    String headerText = getHeaderText();
    String text = headerText == null ? "Invalid header: " + getText() : headerText;

    return new ColoredItemPresentation() {
      @Override
      public String getPresentableText() {
        return text;
      }

      @Override
      public String getLocationString() {
        return null;
      }

      @Override
      public Icon getIcon(final boolean open) {
        return null;
      }

      @Override
      public TextAttributesKey getTextAttributesKey() {
        return getHeaderNumber() == 1 ? MarkdownStructureColors.MARKDOWN_HEADER_BOLD : MarkdownStructureColors.MARKDOWN_HEADER;
      }
    };
  }

  @Nullable
  private String getHeaderText() {
    if (!isValid()) {
      return null;
    }
    final PsiElement contentHolder = findChildByType(MarkdownTokenTypeSets.INLINE_HOLDING_ELEMENT_TYPES);
    if (contentHolder == null) {
      return null;
    }

    return StringUtil.trim(contentHolder.getText());
  }

  public int getHeaderNumber() {
    final IElementType type = getNode().getElementType();
    if (MarkdownTokenTypeSets.HEADER_LEVEL_1_SET.contains(type)) {
      return 1;
    }
    if (MarkdownTokenTypeSets.HEADER_LEVEL_2_SET.contains(type)) {
      return 2;
    }
    if (type == MarkdownElementTypes.ATX_3) {
      return 3;
    }
    if (type == MarkdownElementTypes.ATX_4) {
      return 4;
    }
    if (type == MarkdownElementTypes.ATX_5) {
      return 5;
    }
    if (type == MarkdownElementTypes.ATX_6) {
      return 6;
    }
    throw new IllegalStateException("Type should be one of header types");
  }


  @Override
  public String getName() {
    return getHeaderText();
  }
}
