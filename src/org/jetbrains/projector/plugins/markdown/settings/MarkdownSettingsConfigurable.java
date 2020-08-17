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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.projector.plugins.markdown.MarkdownBundle;
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

public class MarkdownSettingsConfigurable implements SearchableConfigurable {
  public static final Ref<VirtualFile> PLANTUML_JAR_TEST = Ref.create();
  static final String PLANT_UML_DIRECTORY = "plantUML";
  static final String PLANTUML_JAR_URL = Registry.stringValue("markdown.plantuml.download.link");
  //static final String PLANTUML_JAR_URL = "https://downloads.sourceforge.net/project/plantuml/plantuml.jar";
  static final String PLANTUML_JAR = "plantuml.jar";
  private static final String DOWNLOAD_CACHE_DIRECTORY = "download-cache";
  @NotNull
  private final MarkdownApplicationSettings myMarkdownApplicationSettings;
  @Nullable
  private MarkdownSettingsForm myForm = null;

  public MarkdownSettingsConfigurable() {
    myMarkdownApplicationSettings = MarkdownApplicationSettings.getInstance();
  }

  @NotNull
  @Override
  public String getId() {
    return "Settings.Markdown";
  }

  @Nls
  @Override
  public String getDisplayName() {
    return MarkdownBundle.message("markdown.settings.name");
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JLabel(MarkdownBundle.message("markdown.settings.no.providers")), BorderLayout.NORTH);
      return panel;
    }
    return form.getComponent();
  }

  @Nullable
  public MarkdownSettingsForm getForm() {
    if (!MarkdownHtmlPanelProvider.hasAvailableProviders()) {
      return null;
    }

    if (myForm == null) {
      myForm = new MarkdownSettingsForm();
    }
    return myForm;
  }

  @Override
  public boolean isModified() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      return false;
    }
    return !form.getMarkdownCssSettings().equals(myMarkdownApplicationSettings.getMarkdownCssSettings()) ||
           !form.getMarkdownPreviewSettings().equals(myMarkdownApplicationSettings.getMarkdownPreviewSettings()) ||
           form.isDisableInjections() != myMarkdownApplicationSettings.isDisableInjections() ||
           form.isHideErrors() != myMarkdownApplicationSettings.isHideErrors();
  }

  @Override
  public void apply() throws ConfigurationException {
    final MarkdownSettingsForm form = getForm();
    if (form == null) {
      return;
    }

    form.validate();

    myMarkdownApplicationSettings.setMarkdownCssSettings(form.getMarkdownCssSettings());
    myMarkdownApplicationSettings.setMarkdownPreviewSettings(form.getMarkdownPreviewSettings());
    myMarkdownApplicationSettings.setDisableInjections(form.isDisableInjections());
    myMarkdownApplicationSettings.setHideErrors(form.isHideErrors());

    ApplicationManager.getApplication().getMessageBus().syncPublisher(MarkdownApplicationSettings.SettingsChangedListener.TOPIC)
      .settingsChanged(myMarkdownApplicationSettings);
  }

  @Override
  public void reset() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      return;
    }
    form.setMarkdownCssSettings(myMarkdownApplicationSettings.getMarkdownCssSettings());
    form.setMarkdownPreviewSettings(myMarkdownApplicationSettings.getMarkdownPreviewSettings());
    form.setDisableInjections(myMarkdownApplicationSettings.isDisableInjections());
    form.setHideErrors(myMarkdownApplicationSettings.isHideErrors());
  }

  @Override
  public void disposeUIResources() {
    if (myForm != null) {
      Disposer.dispose(myForm);
    }
    myForm = null;
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return "Settings.Markdown";
  }

  /**
   * Returns true if PlantUML jar has been already downloaded
   */
  public static boolean isPlantUMLAvailable() {
    File jarPath = getDownloadedJarPath();
    return jarPath != null && jarPath.exists();
  }

  /**
   * Gets 'download-cache' directory PlantUML jar to be download to
   */
  @NotNull
  public static File getDirectoryToDownload() {
    return new File(PathManager.getSystemPath(), DOWNLOAD_CACHE_DIRECTORY + "/" + PLANT_UML_DIRECTORY);
  }

  /**
   * Returns {@link File} presentation of downloaded PlantUML jar
   */
  @Nullable
  public static File getDownloadedJarPath() {
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      //noinspection TestOnlyProblems
      return Optional.ofNullable(PLANTUML_JAR_TEST.get()).map(VfsUtilCore::virtualToIoFile).orElse(null);
    }
    else {
      return new File(getDirectoryToDownload(), PLANTUML_JAR);
    }
  }
}
