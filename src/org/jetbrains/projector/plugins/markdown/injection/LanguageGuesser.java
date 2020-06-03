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
import com.intellij.lexer.EmbeddedTokenTypesProvider;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LanguageGuesser {
  INSTANCE;

  private final NotNullLazyValue<Map<String, Language>> langIdToLanguage = new NotNullLazyValue<Map<String, Language>>() {
    @NotNull
    @Override
    protected Map<String, Language> compute() {
      final HashMap<String, Language> result = new HashMap<>();
      for (Language language : Language.getRegisteredLanguages()) {
        if (language.getID().isEmpty()) {
          continue;
        }

        result.put(StringUtil.toLowerCase(language.getID()), language);
      }

      return result;
    }
  };

  @NotNull
  public Map<String, Language> getLangToLanguageMap() {
    return Collections.unmodifiableMap(langIdToLanguage.getValue());
  }

  @NotNull
  public List<CodeFenceLanguageProvider> getCodeFenceLanguageProviders() {
    return CodeFenceLanguageProvider.EP_NAME.getExtensionList();
  }

  @Nullable
  public Language guessLanguage(@NotNull String languageName) {
    for (CodeFenceLanguageProvider provider : getCodeFenceLanguageProviders()) {
      final Language languageByProvider = provider.getLanguageByInfoString(languageName);
      if (languageByProvider != null) {
        return languageByProvider;
      }
    }

    final Language languageFromMap = langIdToLanguage.getValue().get(StringUtil.toLowerCase(languageName));
    if (languageFromMap != null) {
      return languageFromMap;
    }

    for (EmbeddedTokenTypesProvider provider : EmbeddedTokenTypesProvider.EXTENSION_POINT_NAME.getExtensionList()) {
      if (provider.getName().equalsIgnoreCase(languageName)) {
        return provider.getElementType().getLanguage();
      }
    }
    return null;
  }
}
