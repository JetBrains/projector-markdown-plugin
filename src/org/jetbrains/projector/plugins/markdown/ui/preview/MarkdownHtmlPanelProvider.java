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

import com.intellij.CommonBundle;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public abstract class MarkdownHtmlPanelProvider {

  public static final ExtensionPointName<MarkdownHtmlPanelProvider> EP_NAME =
    ExtensionPointName.create("org.jetbrains.projector.markdown.html.panel.provider");

  private static MarkdownHtmlPanelProvider[] ourProviders = null;

  @NotNull
  public abstract MarkdownHtmlPanel createHtmlPanel();

  @NotNull
  public abstract AvailabilityInfo isAvailable();

  @NotNull
  public abstract ProviderInfo getProviderInfo();

  public static MarkdownHtmlPanelProvider[] getProviders() {
    if (ourProviders == null) {
      ourProviders = EP_NAME.getExtensions();
    }
    return ourProviders;
  }

  @NotNull
  public static MarkdownHtmlPanelProvider createFromInfo(@NotNull ProviderInfo providerInfo) {
    try {
      return ((MarkdownHtmlPanelProvider)Class.forName(providerInfo.getClassName()).newInstance());
    }
    catch (Exception e) {
      Messages.showMessageDialog(
        "Cannot set preview panel provider (" + providerInfo.getName() + "):\n" + e.getMessage(),
        CommonBundle.getErrorTitle(),
        Messages.getErrorIcon()
      );
      Logger.getInstance(MarkdownHtmlPanelProvider.class).error(e);
      return getProviders()[0];
    }
  }

  public static boolean hasAvailableProviders() {
    return Arrays.stream(getProviders()).anyMatch(provider -> provider.isAvailable() == AvailabilityInfo.AVAILABLE);
  }

  public static class ProviderInfo {
    @NotNull
    @Attribute("name")
    private final String myName;
    @NotNull
    @Attribute("className")
    private final String className;

    @SuppressWarnings("unused")
    private ProviderInfo() {
      myName = "";
      className = "";
    }

    public ProviderInfo(@NotNull String name, @NotNull String className) {
      myName = name;
      this.className = className;
    }

    @NotNull
    public String getName() {
      return myName;
    }

    @NotNull
    public String getClassName() {
      return className;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ProviderInfo info = (ProviderInfo)o;

      if (!myName.equals(info.myName)) return false;
      return className.equals(info.className);
    }

    @Override
    public int hashCode() {
      int result = myName.hashCode();
      result = 31 * result + className.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return myName;
    }
  }

  public static abstract class AvailabilityInfo {
    public static final AvailabilityInfo AVAILABLE = new AvailabilityInfo() {
      @Override
      public boolean checkAvailability(@NotNull JComponent parentComponent) {
        return true;
      }
    };

    public static final AvailabilityInfo UNAVAILABLE = new AvailabilityInfo() {
      @Override
      public boolean checkAvailability(@NotNull JComponent parentComponent) {
        return false;
      }
    };

    public abstract boolean checkAvailability(@NotNull JComponent parentComponent);
  }
}
