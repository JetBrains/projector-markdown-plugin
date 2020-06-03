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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.net.URL;

public final class MarkdownCssSettings {
  public static final MarkdownCssSettings DEFAULT = new MarkdownCssSettings(false);
  public static final MarkdownCssSettings DARCULA = new MarkdownCssSettings(true);

  @SuppressWarnings("FieldMayBeFinal")
  @Attribute("UriEnabled")
  private boolean myUriEnabled;

  @SuppressWarnings("FieldMayBeFinal")
  @Attribute("StylesheetUri")
  @NotNull
  private String myStylesheetUri;

  @SuppressWarnings("FieldMayBeFinal")
  @Attribute("TextEnabled")
  private boolean myTextEnabled;

  @SuppressWarnings("FieldMayBeFinal")
  @Attribute("StylesheetText")
  @NotNull
  private String myStylesheetText;

  private MarkdownCssSettings() {
    this(StartupUiUtil.isUnderDarcula());
  }

  private MarkdownCssSettings(boolean isDarcula) {
    this(false, getPredefinedCssURI(isDarcula), false, "");
  }

  public MarkdownCssSettings(boolean uriEnabled, @NotNull String stylesheetUri, boolean textEnabled, @NotNull String stylesheetText) {
    myUriEnabled = uriEnabled;
    myStylesheetUri = stylesheetUri;
    myTextEnabled = textEnabled;
    myStylesheetText = stylesheetText;
  }

  public boolean isUriEnabled() {
    return myUriEnabled;
  }

  @NotNull
  public String getStylesheetUri() {
    return myStylesheetUri;
  }

  public boolean isTextEnabled() {
    return myTextEnabled;
  }

  @NotNull
  public String getStylesheetText() {
    return myStylesheetText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MarkdownCssSettings settings = (MarkdownCssSettings)o;

    if (myUriEnabled != settings.myUriEnabled) return false;
    if (myTextEnabled != settings.myTextEnabled) return false;
    if (!myStylesheetUri.equals(settings.myStylesheetUri)) return false;
    return myStylesheetText.equals(settings.myStylesheetText);
  }

  @Override
  public int hashCode() {
    int result = (myUriEnabled ? 1 : 0);
    result = 31 * result + myStylesheetUri.hashCode();
    result = 31 * result + (myTextEnabled ? 1 : 0);
    result = 31 * result + myStylesheetText.hashCode();
    return result;
  }

  @NotNull
  public static MarkdownCssSettings getDefaultCssSettings(boolean isDarcula) {
    return isDarcula ? DARCULA : DEFAULT;
  }

  @NotNull
  private static String getPredefinedCssURI(boolean isDarcula) {
    final String fileName = isDarcula ? "darcula.css" : "default.css";
    try {
      final URL resource = MarkdownCssSettings.class.getResource(fileName);
      return resource != null ? resource.toURI().toString() : "";
    }
    catch (URISyntaxException e) {
      Logger.getInstance(MarkdownCssSettings.class).error(e);
      return "";
    }
  }

  public interface Holder {
    @NotNull
    MarkdownCssSettings getMarkdownCssSettings();

    void setMarkdownCssSettings(@NotNull MarkdownCssSettings settings);
  }
}
