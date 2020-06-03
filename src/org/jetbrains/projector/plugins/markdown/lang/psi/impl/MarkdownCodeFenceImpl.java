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
package org.jetbrains.projector.plugins.markdown.lang.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownElementVisitor;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.jetbrains.projector.plugins.markdown.lang.psi.MarkdownPsiElementFactory;
import org.jetbrains.projector.plugins.markdown.structureView.MarkdownBasePresentation;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MarkdownCodeFenceImpl extends CompositePsiElement implements PsiLanguageInjectionHost, MarkdownPsiElement {
  public MarkdownCodeFenceImpl(IElementType type) {
    super(type);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownElementVisitor) {
      ((MarkdownElementVisitor)visitor).visitCodeFence(this);
      return;
    }

    super.accept(visitor);
  }


  @Nullable
  public String getFenceLanguage() {
    final PsiElement element = findPsiChildByType(MarkdownTokenTypes.FENCE_LANG);
    if (element == null) {
      return null;
    }
    return element.getText();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new MarkdownBasePresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        if (!isValid()) {
          return null;
        }
        return "Code fence";
      }

      @Nullable
      @Override
      public String getLocationString() {
        if (!isValid()) {
          return null;
        }

        final StringBuilder sb = new StringBuilder();
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
          if (!(child instanceof MarkdownCodeFenceContentImpl)) {
            continue;
          }
          if (sb.length() > 0) {
            sb.append("\\n");
          }
          sb.append(child.getText());

          if (sb.length() >= MarkdownCompositePsiElementBase.PRESENTABLE_TEXT_LENGTH) {
            break;
          }
        }

        return sb.toString();
      }
    };
  }

  @Override
  public boolean isValidHost() {
    return true;
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    return ElementManipulators.handleContentChange(this, text);
  }

  @NotNull
  @Override
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return new LiteralTextEscaper<PsiLanguageInjectionHost>(this) {
      @Override
      public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
        outChars.append(rangeInsideHost.substring(myHost.getText()));
        return true;
      }

      @Override
      public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
        return rangeInsideHost.getStartOffset() + offsetInDecoded;
      }

      @NotNull
      @Override
      public TextRange getRelevantTextRange() {
        return getContentTextRange();
      }

      public TextRange getContentTextRange() {
        final MarkdownCodeFenceContentImpl first = PsiTreeUtil.findChildOfType(myHost, MarkdownCodeFenceContentImpl.class);
        if (first == null) {
          return TextRange.EMPTY_RANGE;
        }

        MarkdownCodeFenceContentImpl last = null;
        for (PsiElement child = myHost.getLastChild(); child != null; child = child.getPrevSibling()) {
          if (child instanceof MarkdownCodeFenceContentImpl) {
            last = ((MarkdownCodeFenceContentImpl)child);
            break;
          }
        }
        assert last != null;

        return TextRange.create(first.getStartOffsetInParent(), last.getStartOffsetInParent() + last.getTextLength());
      }

      @Override
      public boolean isOneLine() {
        return false;
      }
    };
  }

  @Nullable("Null if no document")
  public static String calculateIndent(@NotNull MarkdownPsiElement element) {
    Document document = PsiDocumentManager.getInstance(element.getProject()).getDocument(element.getContainingFile());
    if (document == null) {
      return null;
    }

    int offset = element.getTextOffset();
    int lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset));
    return document.getText(TextRange.create(lineStartOffset, offset)).replaceAll("[^> ]", " ");
  }

  public static class Manipulator extends AbstractElementManipulator<MarkdownCodeFenceImpl> {

    @Override
    public MarkdownCodeFenceImpl handleContentChange(@NotNull MarkdownCodeFenceImpl element, @NotNull TextRange range, String newContent)
      throws IncorrectOperationException {
      if (newContent == null) {
        return null;
      }

      if (newContent.contains("```") || newContent.contains("~~~")) {
        MarkdownPsiElement textElement = MarkdownPsiElementFactory.createTextElement(element.getProject(), newContent);
        return textElement instanceof MarkdownCodeFenceImpl ? (MarkdownCodeFenceImpl)element.replace(textElement) : null;
      }

      String indent = calculateIndent(element);

      if (indent != null && indent.length() > 0) {
        newContent = Arrays.stream(StringUtil.splitByLinesKeepSeparators(newContent))
          .map(line -> line.replaceAll(indent, ""))
          .map(line -> indent + line)
          .collect(Collectors.joining(""));

        if (StringUtil.endsWithLineBreak(newContent)) {
          newContent += indent;
        }
      }

      return (MarkdownCodeFenceImpl)element.replace(MarkdownPsiElementFactory
                                                      .createCodeFence(element.getProject(), element.getFenceLanguage(),
                                                                       Objects.requireNonNull(newContent), indent));
    }
  }
}
