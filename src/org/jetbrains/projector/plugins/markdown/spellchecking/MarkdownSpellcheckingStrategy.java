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
package org.jetbrains.projector.plugins.markdown.spellchecking;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.TokenSet;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;

public class MarkdownSpellcheckingStrategy extends SpellcheckingStrategy {

  public static final TokenSet NO_SPELLCHECKING_TYPES = TokenSet.create(MarkdownElementTypes.CODE_BLOCK,
                                                                        MarkdownElementTypes.CODE_FENCE,
                                                                        MarkdownElementTypes.CODE_SPAN,
                                                                        MarkdownElementTypes.LINK_DESTINATION);

  @NotNull
  @Override
  public Tokenizer getTokenizer(PsiElement element) {
    final ASTNode node = element.getNode();
    if (node == null || node.getElementType() != MarkdownTokenTypes.TEXT) {
      return EMPTY_TOKENIZER;
    }
    if (TreeUtil.findParent(node, NO_SPELLCHECKING_TYPES) != null) {
      return EMPTY_TOKENIZER;
    }

    return TEXT_TOKENIZER;
  }
}
