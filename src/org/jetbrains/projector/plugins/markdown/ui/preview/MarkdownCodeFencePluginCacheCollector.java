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
package org.jetbrains.projector.plugins.markdown.ui.preview;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MarkdownCodeFencePluginCacheCollector {
  @NotNull private final VirtualFile myFile;
  @NotNull private final Collection<File> myAliveCachedFiles = new HashSet<>();

  public MarkdownCodeFencePluginCacheCollector(@NotNull VirtualFile file) {
    myFile = file;
  }

  @NotNull
  public Collection<File> getAliveCachedFiles() {
    return myAliveCachedFiles;
  }

  @NotNull
  public VirtualFile getFile() {
    return myFile;
  }

  public void addAliveCachedFile(@NotNull File file) {
    myAliveCachedFiles.add(file);
  }

  //need to override `equals()`/`hasCode()` to scan cache for the latest `cacheProvider` only, see 'MarkdownCodeFencePluginCache.registerCacheProvider()'
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MarkdownCodeFencePluginCacheCollector collector = (MarkdownCodeFencePluginCacheCollector)o;
    return Objects.equals(myFile, collector.myFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(myFile);
  }
}
