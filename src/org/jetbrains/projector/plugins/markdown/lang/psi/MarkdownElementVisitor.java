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

import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.*;

public class MarkdownElementVisitor extends PsiElementVisitor {
  public void visitMarkdownFile(@NotNull MarkdownFile file) {
    visitFile(file);
  }

  public void visitLinkDestination(@NotNull MarkdownLinkDestinationImpl linkDestination) {
    visitElement(linkDestination);
  }

  public void visitParagraph(@NotNull MarkdownParagraphImpl paragraph) {
    visitElement(paragraph);
  }

  public void visitList(@NotNull MarkdownListImpl list) {
    visitElement(list);
  }

  public void visitTable(@NotNull MarkdownTableImpl table) {
    visitElement(table);
  }

  public void visitBlockQuote(@NotNull MarkdownBlockQuoteImpl blockQuote) {
    visitElement(blockQuote);
  }

  public void visitCodeFence(@NotNull MarkdownCodeFenceImpl codeFence) {
    visitElement(codeFence);
  }

  public void visitHeader(@NotNull MarkdownHeaderImpl header) {
    visitElement(header);
  }
}
