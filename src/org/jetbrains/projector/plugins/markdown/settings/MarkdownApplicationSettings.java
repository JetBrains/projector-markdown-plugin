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

import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.messages.Topic;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "MarkdownApplicationSettings1",
  storages = @Storage("markdown.xml")
)
public final class MarkdownApplicationSettings implements PersistentStateComponent<MarkdownApplicationSettings.State>,
                                                          MarkdownCssSettings.Holder,
                                                          MarkdownPreviewSettings.Holder {

  private State myState = new State();

  public MarkdownApplicationSettings() {
    MarkdownLAFListener lafListener = new MarkdownLAFListener();
    ApplicationManager.getApplication().getMessageBus().connect().subscribe(LafManagerListener.TOPIC, lafListener);
    // Let's init proper CSS scheme
    ApplicationManager.getApplication().invokeLater(() -> lafListener.updateCssSettingsForced(StartupUiUtil.isUnderDarcula()));
  }

  @Nullable
  @Override
  public State getState() {
    return myState;
  }

  @Override
  public void loadState(@NotNull State state) {
    myState = state;
  }

  @NotNull
  @Override
  public MarkdownCssSettings getMarkdownCssSettings() {
    if (MarkdownCssSettings.DARCULA.getStylesheetUri().equals(myState.myCssSettings.getStylesheetUri())
        || MarkdownCssSettings.DEFAULT.getStylesheetUri().equals(myState.myCssSettings.getStylesheetUri())) {
      return new MarkdownCssSettings(false,
                                     "",
                                     myState.myCssSettings.isTextEnabled(),
                                     myState.myCssSettings.getStylesheetText());
    }

    return myState.myCssSettings;
  }

  @Override
  public void setMarkdownCssSettings(@NotNull MarkdownCssSettings settings) {
    ApplicationManager.getApplication().getMessageBus().syncPublisher(SettingsChangedListener.TOPIC).beforeSettingsChanged(this);
    myState.myCssSettings = settings;
  }

  @NotNull
  @Override
  public MarkdownPreviewSettings getMarkdownPreviewSettings() {
    return myState.myPreviewSettings;
  }

  @Override
  public void setMarkdownPreviewSettings(@NotNull MarkdownPreviewSettings settings) {
    ApplicationManager.getApplication().getMessageBus().syncPublisher(SettingsChangedListener.TOPIC).beforeSettingsChanged(this);
    myState.myPreviewSettings = settings;
  }

  public boolean isDisableInjections() {
    return myState.myDisableInjections;
  }

  public void setDisableInjections(boolean disableInjections) {
    myState.myDisableInjections = disableInjections;
  }

  public boolean isHideErrors() {
    return myState.myHideErrors;
  }

  public void setHideErrors(boolean hideErrors) {
    myState.myHideErrors = hideErrors;
  }

  @NotNull
  public static MarkdownApplicationSettings getInstance() {
    return ServiceManager.getService(MarkdownApplicationSettings.class);
  }

  public interface SettingsChangedListener {
    Topic<SettingsChangedListener> TOPIC = Topic.create("MarkdownApplicationSettingsChanged", SettingsChangedListener.class);

    default void beforeSettingsChanged(@NotNull MarkdownApplicationSettings settings) { }

    default void settingsChanged(@NotNull MarkdownApplicationSettings settings) { }
  }

  public static final class State {
    @Property(surroundWithTag = false)
    @NotNull
    private MarkdownCssSettings myCssSettings = MarkdownCssSettings.DEFAULT;

    @Property(surroundWithTag = false)
    @NotNull
    private MarkdownPreviewSettings myPreviewSettings = MarkdownPreviewSettings.DEFAULT;

    @Attribute("DisableInjections")
    private boolean myDisableInjections = false;

    @Attribute("HideErrors")
    private boolean myHideErrors = false;
  }
}
