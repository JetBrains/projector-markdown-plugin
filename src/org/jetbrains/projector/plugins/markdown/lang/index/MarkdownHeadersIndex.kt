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
package org.jetbrains.projector.plugins.markdown.lang.index

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.CommonProcessors
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownHeaderImpl

class MarkdownHeadersIndex : StringStubIndexExtension<MarkdownHeaderImpl>() {
  override fun getKey(): StubIndexKey<String, MarkdownHeaderImpl> = KEY

  companion object {
    val KEY: StubIndexKey<String, MarkdownHeaderImpl> = StubIndexKey.createIndexKey<String, MarkdownHeaderImpl>("markdown.header")

    fun collectFileHeaders(suggestHeaderRef: String, project: Project, psiFile: PsiFile?): Collection<PsiElement> {
      val list = mutableListOf<PsiElement>()
      StubIndex.getInstance().processElements(
        MarkdownHeadersIndex.KEY, suggestHeaderRef, project,
        psiFile?.let { GlobalSearchScope.fileScope(it) },
        MarkdownHeaderImpl::class.java,
        CommonProcessors.CollectProcessor(list)
      )
      return list
    }
  }
}
