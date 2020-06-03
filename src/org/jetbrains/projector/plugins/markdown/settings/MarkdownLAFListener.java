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

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.StartupUiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class MarkdownLAFListener implements LafManagerListener {
  private boolean isLastLAFWasDarcula = StartupUiUtil.isUnderDarcula();

  @Override
  public void lookAndFeelChanged(@NotNull LafManager source) {
    final UIManager.LookAndFeelInfo newLookAndFeel = source.getCurrentLookAndFeel();
    final boolean isNewLookAndFeelDarcula = isDarcula(newLookAndFeel);

    if (isNewLookAndFeelDarcula == isLastLAFWasDarcula) {
      return;
    }

    updateCssSettingsForced(isNewLookAndFeelDarcula);
  }

  public void updateCssSettingsForced(boolean isDarcula) {
    final MarkdownCssSettings currentCssSettings = MarkdownApplicationSettings.getInstance().getMarkdownCssSettings();
    final String stylesheetUri = StringUtil.isEmpty(currentCssSettings.getStylesheetUri())
                                 ? MarkdownCssSettings.getDefaultCssSettings(isDarcula).getStylesheetUri()
                                 : currentCssSettings.getStylesheetUri();

    MarkdownApplicationSettings.getInstance().setMarkdownCssSettings(new MarkdownCssSettings(
      currentCssSettings.isUriEnabled(),
      stylesheetUri,
      currentCssSettings.isTextEnabled(),
      currentCssSettings.getStylesheetText()
    ));
    isLastLAFWasDarcula = isDarcula;

    ApplicationManager.getApplication().getMessageBus().syncPublisher(MarkdownApplicationSettings.SettingsChangedListener.TOPIC)
      .settingsChanged(MarkdownApplicationSettings.getInstance());
  }

  public static boolean isDarcula(@Nullable UIManager.LookAndFeelInfo laf) {
    if (laf == null) {
      return false;
    }
    return laf.getName().contains("Darcula");
  }
}
