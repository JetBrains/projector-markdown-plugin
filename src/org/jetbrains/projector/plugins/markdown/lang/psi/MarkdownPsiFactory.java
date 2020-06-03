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
package org.jetbrains.projector.plugins.markdown.lang.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypeSets;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.*;

public enum MarkdownPsiFactory {
  INSTANCE;

  public PsiElement createElement(@NotNull ASTNode node) {
    final IElementType elementType = node.getElementType();

    if (elementType == MarkdownElementTypes.PARAGRAPH) {
      return new MarkdownParagraphImpl(node);
    }
    if (MarkdownTokenTypeSets.HEADERS.contains(elementType)) {
      return new MarkdownHeaderImpl(node);
    }
    if (elementType == MarkdownElementTypes.CODE_FENCE) {
      return ((MarkdownCodeFenceImpl)node);
    }
    if (MarkdownTokenTypeSets.LISTS.contains(elementType)) {
      return new MarkdownListImpl(node);
    }
    if (elementType == MarkdownElementTypes.LIST_ITEM) {
      return new MarkdownListItemImpl(node);
    }
    if (elementType == MarkdownElementTypes.BLOCK_QUOTE) {
      return new MarkdownBlockQuoteImpl(node);
    }
    if (elementType == MarkdownElementTypes.LINK_DEFINITION) {
      return new MarkdownLinkDefinitionImpl(node);
    }
    if (elementType == MarkdownElementTypes.LINK_DESTINATION) {
      return new MarkdownLinkDestinationImpl(node);
    }
    if (elementType == MarkdownElementTypes.CODE_BLOCK) {
      return new MarkdownCodeBlockImpl(node);
    }
    if (elementType == MarkdownElementTypes.TABLE) {
      return new MarkdownTableImpl(node);
    }
    if (elementType == MarkdownElementTypes.TABLE_ROW || elementType == MarkdownElementTypes.TABLE_HEADER) {
      return new MarkdownTableRowImpl(node);
    }
    if (elementType == MarkdownElementTypes.TABLE_CELL) {
      return new MarkdownTableCellImpl(node);
    }

    return new ASTWrapperPsiElement(node);
  }
}
