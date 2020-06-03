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
package org.jetbrains.projector.plugins.markdown.extensions;

import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.markdown.IElementType;
import org.intellij.markdown.MarkdownElementTypes;
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor;
import org.intellij.markdown.html.GeneratingProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.extensions.plantuml.PlantUMLProvider;
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownCodeFenceGeneratingProvider;
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownCodeFencePluginCacheCollector;

import java.util.Map;

public class CodeFencePluginFlavourDescriptor extends CommonMarkFlavourDescriptor {
  @NotNull
  public Map<IElementType, GeneratingProvider> createHtmlGeneratingProviders(@NotNull MarkdownCodeFencePluginCacheCollector cacheCollector) {
    return ContainerUtil.newHashMap(Pair.create(MarkdownElementTypes.CODE_FENCE,
                                                new MarkdownCodeFenceGeneratingProvider(
                                                  new MarkdownCodeFencePluginGeneratingProvider[]{new PlantUMLProvider(cacheCollector)})));
  }
}
