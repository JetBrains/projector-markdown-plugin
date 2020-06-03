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
package org.jetbrains.projector.plugins.markdown.injection

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.projector.plugins.markdown.MarkdownBundle
import org.jetbrains.projector.plugins.markdown.lang.MarkdownFileType
import org.jetbrains.projector.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl
import org.jetbrains.projector.plugins.markdown.settings.MarkdownApplicationSettings

class MarkdownCodeFenceErrorHighlightingIntention : IntentionAction {
  init {
    val settingsListener = object : MarkdownApplicationSettings.SettingsChangedListener {
      override fun settingsChanged(settings: MarkdownApplicationSettings) =
        ProjectManager.getInstance().openProjects.forEach { project ->
          FileEditorManager.getInstance(project).openFiles
            .filter { file -> file.fileType == MarkdownFileType.INSTANCE }
            .mapNotNull { file -> PsiManager.getInstance(project).findFile(file) }
            .forEach { DaemonCodeAnalyzerImpl.getInstance(project).restart(it) }
        }
    }

    ApplicationManager.getApplication().messageBus.connect().subscribe<MarkdownApplicationSettings.SettingsChangedListener>(
      MarkdownApplicationSettings.SettingsChangedListener.TOPIC, settingsListener
    )
  }

  override fun getText(): String = MarkdownBundle.message("markdown.hide.errors.intention.text")

  override fun getFamilyName(): String = text

  override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
    if (file?.fileType != MarkdownFileType.INSTANCE || MarkdownApplicationSettings.getInstance().isHideErrors) return false

    val element = file?.findElementAt(editor?.caretModel?.offset ?: return false) ?: return false

    return PsiTreeUtil.getParentOfType(element, MarkdownCodeFenceImpl::class.java) != null
  }

  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    setHideErrors(true)

    val notification = Notification(
      "Markdown", MarkdownBundle.message("markdown.hide.errors.notification.title"),
      MarkdownBundle.message("markdown.hide.errors.notification.content"), NotificationType.INFORMATION
    )
    notification.addAction(object : NotificationAction(MarkdownBundle.message("markdown.hide.errors.notification.rollback.action.text")) {
      override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        setHideErrors(false)
        notification.expire()
      }
    })

    notification.notify(project)
  }

  private fun setHideErrors(hideErrors: Boolean) {
    MarkdownApplicationSettings.getInstance().isHideErrors = hideErrors

    ApplicationManager.getApplication().messageBus.syncPublisher<MarkdownApplicationSettings.SettingsChangedListener>(
      MarkdownApplicationSettings.SettingsChangedListener.TOPIC
    ).settingsChanged(
      MarkdownApplicationSettings.getInstance()
    )
  }

  class CodeFenceHighlightErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(element: PsiErrorElement): Boolean {
      val injectedLanguageManager = InjectedLanguageManager.getInstance(element.project)
      if (injectedLanguageManager.getTopLevelFile(element).fileType == MarkdownFileType.INSTANCE
          && injectedLanguageManager.getInjectionHost(element) is MarkdownCodeFenceImpl
      ) {
        return !MarkdownApplicationSettings.getInstance().isHideErrors
      }

      return true
    }
  }

  override fun startInWriteAction(): Boolean = false
}
