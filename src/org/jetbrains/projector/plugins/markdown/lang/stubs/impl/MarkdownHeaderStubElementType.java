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
package org.jetbrains.projector.plugins.markdown.lang.stubs.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.lang.index.MarkdownHeadersIndex;
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownHeaderImpl;
import org.jetbrains.projector.plugins.markdown.lang.stubs.MarkdownStubElementType;

import java.io.IOException;

public class MarkdownHeaderStubElementType extends MarkdownStubElementType<MarkdownHeaderStubElement, MarkdownHeaderImpl> {
  private static final Logger LOG = Logger.getInstance(MarkdownHeaderStubElementType.class);

  public MarkdownHeaderStubElementType(@NotNull String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  public PsiElement createElement(@NotNull ASTNode node) {
    return new MarkdownHeaderImpl(node);
  }

  @Override
  public MarkdownHeaderImpl createPsi(@NotNull MarkdownHeaderStubElement stub) {
    return new MarkdownHeaderImpl(stub, this);
  }

  @NotNull
  @Override
  public MarkdownHeaderStubElement createStub(@NotNull MarkdownHeaderImpl psi, StubElement parentStub) {
    return new MarkdownHeaderStubElement(parentStub, this, psi.getName());
  }

  @Override
  public void serialize(@NotNull MarkdownHeaderStubElement stub, @NotNull StubOutputStream dataStream) throws IOException {
    writeUTFFast(dataStream, stub.getIndexedName());
  }

  @NotNull
  @Override
  public MarkdownHeaderStubElement deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    String indexedName = null;
    try {
      indexedName = dataStream.readUTFFast();
    }
    catch (IOException e) {
      LOG.error("Cannot read data stream; ", e.getMessage());
    }

    String finalIndexedString = StringUtil.isEmpty(indexedName) ? null : indexedName;
    return new MarkdownHeaderStubElement(
      parentStub,
      this,
      finalIndexedString
    );
  }

  @Override
  public void indexStub(@NotNull MarkdownHeaderStubElement stub, @NotNull IndexSink sink) {
    String indexedName = stub.getIndexedName();
    if (indexedName != null) sink.occurrence(MarkdownHeadersIndex.Companion.getKEY(), indexedName);
  }

  private static void writeUTFFast(@NotNull StubOutputStream dataStream, String text) throws IOException {
    if (text == null) text = "";
    dataStream.writeUTFFast(text);
  }
}
