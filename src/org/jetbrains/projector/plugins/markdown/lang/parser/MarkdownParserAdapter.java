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
package org.jetbrains.projector.plugins.markdown.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor;
import org.jetbrains.annotations.NotNull;

public class MarkdownParserAdapter implements PsiParser {
  @NotNull final MarkdownFlavourDescriptor myFlavour;

  public MarkdownParserAdapter() {
    this(MarkdownParserManager.FLAVOUR);
  }

  public MarkdownParserAdapter(@NotNull MarkdownFlavourDescriptor flavour) {
    myFlavour = flavour;
  }

  @Override
  @NotNull
  public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {

    PsiBuilder.Marker rootMarker = builder.mark();

    final org.intellij.markdown.ast.ASTNode parsedTree = MarkdownParserManager.parseContent(builder.getOriginalText(), myFlavour);

    assert builder.getCurrentOffset() == 0;
    new PsiBuilderFillingVisitor(builder).visitNode(parsedTree);
    assert builder.eof();

    rootMarker.done(root);

    return builder.getTreeBuilt();
  }
}
