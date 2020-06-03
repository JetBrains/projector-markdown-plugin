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

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownTokenTypes;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceContentImpl;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl;

public class MarkdownASTFactory extends ASTFactory {
  @Nullable
  @Override
  public CompositeElement createComposite(@NotNull IElementType type) {
    if (type == MarkdownElementTypes.CODE_FENCE) {
      return new MarkdownCodeFenceImpl(type);
    }

    return super.createComposite(type);
  }

  @Nullable
  @Override
  public LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
    if (type == MarkdownTokenTypes.CODE_FENCE_CONTENT) {
      return new MarkdownCodeFenceContentImpl(type, text);
    }
    return super.createLeaf(type, text);
  }
}
