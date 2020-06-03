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
package org.jetbrains.projector.plugins.markdown.settings;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider;
import org.jetbrains.projector.plugins.markdown.ui.preview.projector.ProjectorMarkdownHtmlPanelProvider;
import org.jetbrains.projector.plugins.markdown.ui.split.SplitFileEditor;

public final class MarkdownPreviewSettings {
  public static final MarkdownPreviewSettings DEFAULT = new MarkdownPreviewSettings();

  @Attribute("DefaultSplitLayout")
  @NotNull
  private SplitFileEditor.SplitEditorLayout mySplitEditorLayout = SplitFileEditor.SplitEditorLayout.SPLIT;

  @Tag("HtmlPanelProviderInfo")
  @Property(surroundWithTag = false)
  @NotNull
  private MarkdownHtmlPanelProvider.ProviderInfo myHtmlPanelProviderInfo =
    new ProjectorMarkdownHtmlPanelProvider().getProviderInfo();  // set our provider as in the original plugin

  @Attribute("UseGrayscaleRendering")
  private boolean myUseGrayscaleRendering = true;

  @Attribute("AutoScrollPreview")
  private boolean myIsAutoScrollPreview = true;

  @Attribute("VerticalSplit")
  private boolean myIsVerticalSplit = true;

  public MarkdownPreviewSettings() {
  }

  public MarkdownPreviewSettings(@NotNull SplitFileEditor.SplitEditorLayout splitEditorLayout,
                                 @NotNull MarkdownHtmlPanelProvider.ProviderInfo htmlPanelProviderInfo,
                                 boolean useGrayscaleRendering,
                                 boolean isAutoScrollPreview,
                                 boolean isVerticalSplit) {
    mySplitEditorLayout = splitEditorLayout;
    myHtmlPanelProviderInfo = htmlPanelProviderInfo;
    myUseGrayscaleRendering = useGrayscaleRendering;
    myIsAutoScrollPreview = isAutoScrollPreview;
    myIsVerticalSplit = isVerticalSplit;
  }

  @NotNull
  public SplitFileEditor.SplitEditorLayout getSplitEditorLayout() {
    return mySplitEditorLayout;
  }

  @NotNull
  public MarkdownHtmlPanelProvider.ProviderInfo getHtmlPanelProviderInfo() {
    return myHtmlPanelProviderInfo;
  }

  public boolean isUseGrayscaleRendering() {
    return myUseGrayscaleRendering;
  }

  public boolean isAutoScrollPreview() {
    return myIsAutoScrollPreview;
  }

  public boolean isVerticalSplit() {
    return myIsVerticalSplit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MarkdownPreviewSettings settings = (MarkdownPreviewSettings)o;

    if (myUseGrayscaleRendering != settings.myUseGrayscaleRendering) return false;
    if (myIsAutoScrollPreview != settings.myIsAutoScrollPreview) return false;
    if (myIsVerticalSplit != settings.myIsVerticalSplit) return false;
    if (mySplitEditorLayout != settings.mySplitEditorLayout) return false;
    return myHtmlPanelProviderInfo.equals(settings.myHtmlPanelProviderInfo);
  }

  @Override
  public int hashCode() {
    int result = mySplitEditorLayout.hashCode();
    result = 31 * result + myHtmlPanelProviderInfo.hashCode();
    result = 31 * result + (myUseGrayscaleRendering ? 1 : 0);
    result = 31 * result + (myIsAutoScrollPreview ? 1 : 0);
    result = 31 * result + (myIsVerticalSplit ? 1 : 0);
    return result;
  }

  public interface Holder {
    @NotNull
    MarkdownPreviewSettings getMarkdownPreviewSettings();

    void setMarkdownPreviewSettings(@NotNull MarkdownPreviewSettings settings);
  }
}
