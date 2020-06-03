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
package org.jetbrains.projector.plugins.markdown.lang.references;

import com.intellij.openapi.paths.PathReference;
import com.intellij.openapi.paths.PathReferenceProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;

import java.util.List;

public class MarkdownAnchorPathReferenceProvider implements PathReferenceProvider {
  @Override
  public boolean createReferences(@NotNull final PsiElement psiElement, @NotNull final List<PsiReference> references, final boolean soft) {
    if (!(psiElement instanceof MarkdownPsiElement)) return false;

    final TextRange range = ElementManipulators.getValueTextRange(psiElement);
    final String elementText = psiElement.getText();
    final int anchorOffset = elementText.indexOf('#');
    if (anchorOffset == -1) return false;

    FileReference fileReference = null;
    if (range.getStartOffset() != anchorOffset) {
      fileReference = findFileReference(references);
      if (fileReference == null || fileReference.resolve() == null) return false;
    }

    final String anchor;
    try {
      int endIndex = range.getEndOffset();
      if (endIndex <= anchorOffset) endIndex = anchorOffset + 1;
      anchor = elementText.substring(anchorOffset + 1, endIndex);
    }
    catch (StringIndexOutOfBoundsException e) {
      throw new RuntimeException(elementText, e);
    }

    references.add(new MarkdownAnchorReferenceImpl(anchor, fileReference, psiElement, anchorOffset + 1));
    return false;
  }

  @Override
  public PathReference getPathReference(@NotNull final String path, @NotNull final PsiElement element) {
    return null;
  }

  @Nullable
  private static FileReference findFileReference(final List<PsiReference> references) {
    FileReference fileReference = null;
    for (PsiReference reference : references) {
      if (reference instanceof FileReference) {
        fileReference = ((FileReference)reference).getFileReferenceSet().getLastReference();
        break;
      }
    }
    return fileReference;
  }
}
