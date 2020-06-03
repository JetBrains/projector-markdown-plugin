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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownLanguage;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.*;

public class MarkdownPsiElementFactory {
  private MarkdownPsiElementFactory() { }

  @NotNull
  public static MarkdownFile createFile(@NotNull Project project, @NotNull String text) {
    final LightVirtualFile virtualFile = new LightVirtualFile("temp.rb", MarkdownLanguage.INSTANCE, text);
    PsiFile psiFile = ((PsiFileFactoryImpl)PsiFileFactory.getInstance(project))
      .trySetupPsiForFile(virtualFile, MarkdownLanguage.INSTANCE, true, true);

    if (!(psiFile instanceof MarkdownFile)) {
      throw new RuntimeException("Cannot create a new markdown file. Text: " + text);
    }

    return (MarkdownFile)psiFile;
  }


  @NotNull
  public static MarkdownCodeFenceImpl createCodeFence(@NotNull Project project, @Nullable String language, @NotNull String text) {
    return createCodeFence(project, language, text, null);
  }


  @NotNull
  public static MarkdownCodeFenceImpl createCodeFence(@NotNull Project project,
                                                      @Nullable String language,
                                                      @NotNull String text,
                                                      @Nullable String indent) {
    text = StringUtil.isEmpty(text) ? "" : "\n" + text;
    String content = "```" + StringUtil.notNullize(language) + text + "\n" + StringUtil.notNullize(indent) + "```";
    final MarkdownFile file = createFile(project, content);

    return (MarkdownCodeFenceImpl)file.getFirstChild().getFirstChild();
  }

  @NotNull
  public static MarkdownPsiElement createTextElement(@NotNull Project project, @NotNull String text) {
    return (MarkdownPsiElement)createFile(project, text).getFirstChild().getFirstChild();
  }

  @NotNull
  public static MarkdownHeaderImpl createSetext(@NotNull Project project, @NotNull String text, @NotNull String symbol, int count) {
    return (MarkdownHeaderImpl)createFile(project, text + "\n" + StringUtil.repeat(symbol, count)).getFirstChild().getFirstChild();
  }

  @NotNull
  public static MarkdownHeaderImpl createHeader(@NotNull Project project, @NotNull String text, int level) {
    return (MarkdownHeaderImpl)createFile(project, StringUtil.repeat("#", level) + " " + text).getFirstChild().getFirstChild();
  }

  @NotNull
  public static PsiElement createNewLine(@NotNull Project project) {
    return createFile(project, "\n").getFirstChild().getFirstChild();
  }

  /**
   * Returns pair of the link reference and its declaration
   */
  @NotNull
  public static Pair<PsiElement, PsiElement> createLinkDeclarationAndReference(@NotNull Project project,
                                                                               @NotNull String url,
                                                                               @NotNull String text,
                                                                               @Nullable String title,
                                                                               @NotNull String reference) {
    text = ObjectUtils.notNull(text, reference);
    title = title == null ? "" : " " + title;

    String linkReference = "[" + text + "][" + reference + "]" + "\n\n" + "[" + reference + "]" + ": " + url + title;

    PsiElement linkReferenceElement = createFile(project, linkReference).getFirstChild();

    PsiElement ref = linkReferenceElement.getFirstChild();
    assert ref instanceof MarkdownParagraphImpl;

    PsiElement declaration = linkReferenceElement.getLastChild();
    assert declaration instanceof MarkdownParagraphImpl || declaration instanceof MarkdownLinkDefinitionImpl;

    return Pair.create(ref, declaration);
  }
}
