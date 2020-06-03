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
package org.jetbrains.projector.plugins.markdown.lang.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.intellij.markdown.ast.ASTNode;
import org.intellij.markdown.ast.ASTNodeKt;
import org.intellij.markdown.ast.visitors.RecursiveVisitor;
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.lang.MarkdownElementType;
import org.jetbrains.projector.plugins.markdown.lang.parser.MarkdownParserManager;

import java.util.ArrayList;
import java.util.List;

public class MarkdownToplevelLexer extends LexerBase {
  @NotNull final MarkdownFlavourDescriptor myFlavour;
  private CharSequence myBuffer;
  private int myBufferStart;
  private int myBufferEnd;
  private List<IElementType> myLexemes;
  private List<Integer> myStartOffsets;
  private List<Integer> myEndOffsets;
  private int myLexemeIndex;

  public MarkdownToplevelLexer() {
    this(MarkdownParserManager.FLAVOUR);
  }

  public MarkdownToplevelLexer(@NotNull MarkdownFlavourDescriptor flavour) {
    myFlavour = flavour;
  }

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    myBuffer = buffer;
    myBufferStart = startOffset;
    myBufferEnd = endOffset;

    final ASTNode parsedTree = MarkdownParserManager.parseContent(buffer.subSequence(startOffset, endOffset), myFlavour);
    myLexemes = new ArrayList<>();
    myStartOffsets = new ArrayList<>();
    myEndOffsets = new ArrayList<>();
    ASTNodeKt.accept(parsedTree, new LexerBuildingVisitor());
    myLexemeIndex = 0;
  }

  @Override
  public int getState() {
    return myLexemeIndex;
  }

  @Nullable
  @Override
  public IElementType getTokenType() {
    if (myLexemeIndex >= myLexemes.size()) {
      return null;
    }
    return myLexemes.get(myLexemeIndex);
  }

  @Override
  public int getTokenStart() {
    if (myLexemeIndex >= myLexemes.size()) {
      return myBufferEnd;
    }
    return myBufferStart + myStartOffsets.get(myLexemeIndex);
  }

  @Override
  public int getTokenEnd() {
    if (myLexemeIndex >= myLexemes.size()) {
      return myBufferEnd;
    }
    return myBufferStart + myEndOffsets.get(myLexemeIndex);
  }

  @Override
  public void advance() {
    myLexemeIndex++;
  }

  @NotNull
  @Override
  public CharSequence getBufferSequence() {
    return myBuffer;
  }

  @Override
  public int getBufferEnd() {
    return myBufferEnd;
  }

  private class LexerBuildingVisitor extends RecursiveVisitor {

    @Override
    public void visitNode(@NotNull ASTNode node) {
      if (node.getStartOffset() == node.getEndOffset()) {
        return;
      }
      final List<ASTNode> children = node.getChildren();
      if (children.isEmpty()) {
        myLexemes.add(MarkdownElementType.platformType(node.getType()));
        myStartOffsets.add(node.getStartOffset());
        myEndOffsets.add(node.getEndOffset());
      }
      else {
        super.visitNode(node);
      }
    }
  }
}
