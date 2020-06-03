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
package org.jetbrains.projector.plugins.markdown.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor;
import org.intellij.markdown.parser.MarkdownParser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.lexer.MarkdownMergingLexer;
import org.jetbrains.projector.plugins.markdown.lang.parser.MarkdownParserManager;
import org.jetbrains.projector.plugins.markdown.lang.parser.PsiBuilderFillingVisitor;

public class MarkdownLazyElementType extends ILazyParseableElementType {
  private static final Logger LOG = Logger.getInstance(MarkdownLazyElementType.class);

  public MarkdownLazyElementType(@NotNull @NonNls String debugName) {
    super(debugName, MarkdownLanguage.INSTANCE);
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    final Project project = psi.getProject();
    final Lexer lexer = new MarkdownMergingLexer();
    final CharSequence chars = chameleon.getChars();

    MarkdownFlavourDescriptor flavour = psi.getContainingFile().getUserData(MarkdownParserManager.FLAVOUR_DESCRIPTION);
    if (flavour == null) {
      LOG.error("Markdown flavour doesn't set for " + psi.getContainingFile());
      flavour = MarkdownParserManager.FLAVOUR;
    }

    final org.intellij.markdown.ast.ASTNode node = new MarkdownParser(flavour)
      .parseInline(MarkdownElementType.markdownType(chameleon.getElementType()), chars, 0, chars.length());

    final PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, lexer, getLanguage(), chars);
    assert builder.getCurrentOffset() == 0;
    new PsiBuilderFillingVisitor(builder).visitNode(node);
    assert builder.eof();

    return builder.getTreeBuilt().getFirstChildNode();
  }
}
