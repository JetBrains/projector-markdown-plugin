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

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Range;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.markdown.html.HtmlGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.settings.MarkdownCssSettings;
import org.w3c.dom.Node;

import javax.swing.*;
import java.util.List;

public interface MarkdownHtmlPanel extends Disposable {
  List<String> SCRIPTS = ContainerUtil.immutableList("processLinks.js", "scrollToElement.js");

  //List<String> STYLES = ContainerUtil.immutableList("default.css", "darcula.css", PreviewStaticServer.INLINE_CSS_FILENAME);

  @NotNull
  JComponent getComponent();

  void setHtml(@NotNull String html);

  void setCSS(@Nullable String inlineCss, String... fileUris);

  void render();

  void scrollToMarkdownSrcOffset(int offset);

  @Nullable
  static Range<Integer> nodeToSrcRange(@NotNull Node node) {
    if (!node.hasAttributes()) {
      return null;
    }
    final Node attribute = node.getAttributes().getNamedItem(HtmlGenerator.Companion.getSRC_ATTRIBUTE_NAME());
    if (attribute == null) {
      return null;
    }
    final List<String> startEnd = StringUtil.split(attribute.getNodeValue(), "..");
    if (startEnd.size() != 2) {
      return null;
    }
    return new Range<>(Integer.parseInt(startEnd.get(0)), Integer.parseInt(startEnd.get(1)));
  }

  @NotNull
  static String getCssLines(@Nullable String inlineCss, String... fileUris) {
    StringBuilder result = new StringBuilder();

    for (String uri : fileUris) {
      uri = migrateUriToHttp(uri);
      result.append("<link rel=\"stylesheet\" href=\"").append(uri).append("\" />\n");
    }
    if (inlineCss != null) {
      result.append("<style>\n").append(inlineCss).append("\n</style>\n");
    }
    return result.toString();
  }

  static String migrateUriToHttp(@NotNull String uri) {
    if (uri.equals(MarkdownCssSettings.DEFAULT.getStylesheetUri())) {
      return "URL for default.css";
      //return PreviewStaticServer.getStyleUrl("default.css");
    }
    else if (uri.equals(MarkdownCssSettings.DARCULA.getStylesheetUri())) {
      return "URL for darcula.css";
      //return PreviewStaticServer.getStyleUrl("darcula.css");
    }
    else {
      return uri;
    }
  }
}
