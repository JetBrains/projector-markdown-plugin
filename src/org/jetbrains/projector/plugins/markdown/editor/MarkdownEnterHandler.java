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
package org.jetbrains.projector.plugins.markdown.editor;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownBlockQuoteImpl;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownFile;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownListItemImpl;

public class MarkdownEnterHandler extends EnterHandlerDelegateAdapter {
  @Override
  public Result preprocessEnter(@NotNull PsiFile file,
                                @NotNull Editor editor,
                                @NotNull Ref<Integer> caretOffset,
                                @NotNull Ref<Integer> caretAdvance,
                                @NotNull DataContext dataContext,
                                EditorActionHandler originalHandler) {
    PsiElement psiElement = file.findElementAt(caretOffset.get() - 1);
    if (psiElement == null) {
      return Result.Continue;
    }

    if (!shouldHandle(editor, dataContext, psiElement)) {
      return Result.Continue;
    }

    if (processCodeFence(editor, psiElement)) return Result.Stop;
    if (processBlockQuote(editor, psiElement)) return Result.Stop;

    return Result.Continue;
  }

  private static boolean processBlockQuote(@NotNull Editor editor, @NotNull PsiElement element) {
    MarkdownBlockQuoteImpl blockQuote = PsiTreeUtil.getParentOfType(element, MarkdownBlockQuoteImpl.class);
    if (blockQuote != null) {
      MarkdownListItemImpl listItem = PsiTreeUtil.getParentOfType(blockQuote, MarkdownListItemImpl.class);
      if (listItem == null) {
        EditorModificationUtil.insertStringAtCaret(editor, "\n>");
      }
      else {
        String indent = StringUtil.repeat(" ", blockQuote.getTextOffset() - listItem.getTextOffset());
        EditorModificationUtil.insertStringAtCaret(editor, "\n" + indent + ">");
      }
      return true;
    }

    return false;
  }

  private static boolean processCodeFence(@NotNull Editor editor, @NotNull PsiElement element) {
    PsiLanguageInjectionHost codeFence = InjectedLanguageManager.getInstance(element.getProject()).getInjectionHost(element);
    if (!(codeFence instanceof MarkdownCodeFenceImpl)) {
      codeFence = PsiTreeUtil.getParentOfType(element, MarkdownCodeFenceImpl.class);
    }

    if (codeFence != null) {
      EditorModificationUtil.insertStringAtCaret(editor, "\n" + MarkdownCodeFenceImpl.calculateIndent((MarkdownPsiElement)codeFence));
      return true;
    }

    return false;
  }

  private static boolean shouldHandle(@NotNull Editor editor, @NotNull DataContext dataContext, @NotNull PsiElement element) {
    final Project project = CommonDataKeys.PROJECT.getData(dataContext);
    if (project == null) {
      return false;
    }

    Document editorDocument = editor.getDocument();
    if (!editorDocument.isWritable()) {
      return false;
    }

    PsiFile topLevelFile = InjectedLanguageManager.getInstance(project).getTopLevelFile(element);
    if (!(topLevelFile instanceof MarkdownFile)) {
      return false;
    }

    return !editor.isViewer();
  }
}
