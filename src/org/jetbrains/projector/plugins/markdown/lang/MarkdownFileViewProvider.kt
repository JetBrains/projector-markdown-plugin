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
package org.jetbrains.projector.plugins.markdown.lang

import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import gnu.trove.THashSet
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes.MARKDOWN_TEMPLATE_DATA
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownFile

class MarkdownFileViewProvider(manager: PsiManager, virtualFile: VirtualFile, eventSystemEnabled: Boolean) :
  MultiplePsiFilesPerDocumentFileViewProvider(manager, virtualFile, eventSystemEnabled), TemplateLanguageFileViewProvider {

  private val myRelevantLanguages = THashSet<Language>()

  init {
    myRelevantLanguages.add(baseLanguage)
    myRelevantLanguages.add(templateDataLanguage)
  }

  override fun createFile(lang: Language): PsiFile? {
    if (lang === MarkdownLanguage.INSTANCE) {
      return MarkdownFile(this)
    }

    val parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang) ?: return null

    val psiFile = parserDefinition.createFile(this)
    if (lang === templateDataLanguage && psiFile is PsiFileImpl) {
      psiFile.contentElementType = MARKDOWN_TEMPLATE_DATA
    }

    return psiFile
  }

  override fun getBaseLanguage(): Language = MarkdownLanguage.INSTANCE

  override fun getLanguages(): Set<Language> = myRelevantLanguages

  override fun getTemplateDataLanguage(): Language = HTMLLanguage.INSTANCE

  override fun cloneInner(fileCopy: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider =
    MarkdownFileViewProvider(manager, fileCopy, false)
}

class MarkdownFileViewProviderFactory : FileViewProviderFactory {
  override fun createFileViewProvider(
    file: VirtualFile,
    language: Language,
    manager: PsiManager,
    eventSystemEnabled: Boolean
  ): FileViewProvider =
    MarkdownFileViewProvider(manager, file, eventSystemEnabled)
}
