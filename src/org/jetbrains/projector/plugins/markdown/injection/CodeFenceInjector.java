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
package org.jetbrains.projector.plugins.markdown.injection;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceContentImpl;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl;
import org.jetbrains.projector.plugins.markdown.settings.MarkdownApplicationSettings;

import java.util.Collections;
import java.util.List;

public class CodeFenceInjector implements MultiHostInjector {
  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    if (!(context instanceof MarkdownCodeFenceImpl)) {
      return;
    }
    if (PsiTreeUtil.findChildOfType(context, MarkdownCodeFenceContentImpl.class) == null) {
      return;
    }

    final Language language = findLangForInjection(((MarkdownCodeFenceImpl)context));
    if (language == null || LanguageParserDefinitions.INSTANCE.forLanguage(language) == null) {
      return;
    }

    registrar.startInjecting(language);
    for (PsiElement child = context.getFirstChild().getNextSibling().getNextSibling(); child != null; child = child.getNextSibling()) {
      if (child.getNode().getElementType() == MarkdownTokenTypes.EOL) {
        registrar.addPlace(null, null, ((MarkdownCodeFenceImpl)context), TextRange.from(child.getStartOffsetInParent(), 1));
        continue;
      }

      if (child instanceof MarkdownCodeFenceContentImpl) {
        PsiElement nextSibling = child.getNextSibling();
        boolean includeNewLine = nextSibling != null && nextSibling.getNode().getElementType() == MarkdownTokenTypes.EOL;
        registrar.addPlace(null, null, ((MarkdownCodeFenceImpl)context), TextRange
          .from(child.getStartOffsetInParent(), includeNewLine ? child.getTextLength() + 1 : child.getTextLength()));

        if (includeNewLine) {
          //noinspection AssignmentToForLoopParameter
          child = nextSibling;
        }
      }
    }
    registrar.doneInjecting();
  }

  @NotNull
  @Override
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return Collections.singletonList(MarkdownCodeFenceImpl.class);
  }

  @Nullable
  protected Language findLangForInjection(@NotNull MarkdownCodeFenceImpl element) {
    final String fenceLanguage = element.getFenceLanguage();
    if (fenceLanguage == null) {
      return null;
    }
    return guessLanguageByFenceLang(fenceLanguage);
  }

  @Nullable
  private static Language guessLanguageByFenceLang(@NotNull String langName) {
    if (MarkdownApplicationSettings.getInstance().isDisableInjections()) {
      return null;
    }
    else {
      return LanguageGuesser.INSTANCE.guessLanguage(langName);
    }
  }
}
