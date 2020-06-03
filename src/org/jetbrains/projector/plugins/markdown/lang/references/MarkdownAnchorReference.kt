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

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.stubs.StubIndex
import org.jetbrains.projector.plugins.markdown.lang.index.MarkdownHeadersIndex

interface MarkdownAnchorReference : PsiPolyVariantReference {
  companion object {
    fun getPsiHeaders(project: Project, text: String, psiFile: PsiFile?): Collection<PsiElement> {
      // optimization: trying to find capitalized header
      val suggestedHeader = StringUtil.replace(text, "-", " ")
      var headers: Collection<PsiElement> =
        MarkdownHeadersIndex.collectFileHeaders(StringUtil.capitalize(suggestedHeader), project, psiFile)
      if (headers.isNotEmpty()) return headers

      headers = MarkdownHeadersIndex.collectFileHeaders(StringUtil.capitalizeWords(suggestedHeader, true), project, psiFile)
      if (headers.isNotEmpty()) return headers

      // header search
      headers = StubIndex.getInstance().getAllKeys(MarkdownHeadersIndex.KEY, project)
        .filter { dashed(it) == text }
        .flatMap { MarkdownHeadersIndex.collectFileHeaders(it, project, psiFile) }

      return headers
    }

    fun dashed(it: String): String =
      it.toLowerCase()
        .trimStart()
        .replace(Regex("[^\\w\\- ]"), "")
        .replace(" ", "-")
  }
}
