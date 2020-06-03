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
package org.jetbrains.projector.plugins.markdown.lang.references

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processor
import org.jetbrains.projector.plugins.markdown.MarkdownBundle
import org.jetbrains.projector.plugins.markdown.lang.index.MarkdownHeadersIndex
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownHeaderImpl

class MarkdownAnchorReferenceImpl internal constructor(
  private val myAnchor: String,
  private val myFileReference: FileReference?,
  private val myPsiElement: PsiElement,
  private val myOffset: Int
) : MarkdownAnchorReference, PsiPolyVariantReferenceBase<PsiElement>(
  myPsiElement
), EmptyResolveMessageProvider {
  private val file: PsiFile?
    get() = if (myFileReference != null) myFileReference.resolve() as? PsiFile else myPsiElement.containingFile.originalFile

  override fun getElement(): PsiElement = myPsiElement

  override fun getRangeInElement(): TextRange = TextRange(myOffset, myOffset + myAnchor.length)

  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    if (myAnchor.isEmpty()) return PsiElementResolveResult.createResults(myPsiElement)

    val project = myPsiElement.project

    return PsiElementResolveResult.createResults(MarkdownAnchorReference.getPsiHeaders(project, canonicalText, file))
  }

  override fun getCanonicalText(): String = myAnchor

  override fun getVariants(): Array<Any> {
    val project = myPsiElement.project
    val list = mutableListOf<String>()

    StubIndex.getInstance().getAllKeys(MarkdownHeadersIndex.KEY, project)
      .forEach { key ->
        StubIndex.getInstance().processElements(MarkdownHeadersIndex.KEY, key, project,
                                                file?.let { GlobalSearchScope.fileScope(it) },
                                                MarkdownHeaderImpl::class.java,
                                                Processor { list.add(MarkdownAnchorReference.dashed(key)) }
        )
      }

    return list.toTypedArray()
  }

  override fun getUnresolvedMessagePattern(): String = if (file == null)
    MarkdownBundle.message("markdown.cannot.resolve.anchor.error.message", myAnchor)
  else
    MarkdownBundle.message("markdown.cannot.resolve.anchor.in.file.error.message", myAnchor, (file as PsiFile).name)
}
