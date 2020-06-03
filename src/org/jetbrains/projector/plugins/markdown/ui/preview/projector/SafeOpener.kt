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
package org.jetbrains.projector.plugins.markdown.ui.preview.projector

import com.intellij.ide.BrowserUtil
import com.intellij.ide.actions.OpenFileAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectForContentFile
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopupStep
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.ThrowableComputable
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.PsiNavigateUtil
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.io.isLocalHost
import org.jetbrains.projector.plugins.markdown.MarkdownBundle
import org.jetbrains.projector.plugins.markdown.lang.references.MarkdownAnchorReference
import org.jetbrains.projector.plugins.markdown.ui.preview.MarkdownSplitEditor
import java.net.URI
import java.net.URISyntaxException
import java.util.*

// adopted from SafeOpener.java
object SafeOpener {

  fun openInExternalBrowser(link: String) {
    val uri = try {
      if (!BrowserUtil.isAbsoluteURL(link)) {
        URI("http://$link")
      }
      else {
        URI(link)
      }
    }
    catch (e: URISyntaxException) {
      LOG.info(e)
      return
    }

    if (tryOpenInEditor(uri)) {
      return
    }
    if (!isHttpScheme(uri.scheme) || isLocalHost(uri.host) && !isSafeExtension(uri.path)) {
      LOG.warn("Bad URL", InaccessibleURLOpenedException(link))
      return
    }

    // todo: maybe we can use initial BrowserUtil.browse(uri) by implementing something so we can stop copying SafeOpener
    ProjectorMarkdownHtmlPanelProvider.browseUriCallback?.accept(uri.toASCIIString())
  }

  private val SCHEMES: Set<String> = ContainerUtil.newTroveSet(
    "http",
    "https"
  )

  private fun isHttpScheme(scheme: String?): Boolean {
    return scheme != null && SCHEMES.contains(StringUtil.toLowerCase(scheme))
  }

  private fun isLocalHost(hostName: String?): Boolean {
    return (hostName == null || hostName.startsWith("127.")
            || hostName.endsWith(":1")
            || isLocalHost(hostName, onlyAnyOrLoopback = false, hostsOnly = false))
  }

  private val SAFE_LOCAL_EXTENSIONS: Set<String> = ContainerUtil.newTroveSet(
    "md",
    "png",
    "gif",
    "jpg",
    "jpeg",
    "bmp",
    "svg",
    "html"
  )

  private fun isSafeExtension(path: String?): Boolean {
    if (path == null) {
      return false
    }
    val i = path.lastIndexOf('.')
    return i != -1 && SAFE_LOCAL_EXTENSIONS.contains(StringUtil.toLowerCase(path.substring(i + 1)))
  }

  private fun tryOpenInEditor(uri: URI): Boolean {
    return if ("file" != uri.scheme) {
      false
    }
    else ReadAction.compute<Boolean, RuntimeException> {
      val anchor = uri.fragment
      val path = uri.path
      val targetFile = LocalFileSystem.getInstance().findFileByPath(path) ?: return@compute false
      val project: Project = guessProjectForContentFile(targetFile) ?: return@compute false
      if (anchor == null) {
        ApplicationManager.getApplication().invokeLater {
          OpenFileAction.openFile(targetFile, project)
        }
        return@compute true
      }
      val frame = Objects.requireNonNull(WindowManager.getInstance().getFrame(project))!!
      val mousePosition = frame.mousePosition ?: return@compute false
      val point = RelativePoint(frame, mousePosition)
      ApplicationManager.getApplication().invokeLater {
        val headers: Collection<PsiElement> = ReadAction.compute(
          ThrowableComputable {
            MarkdownAnchorReference.getPsiHeaders(project, anchor, PsiManager.getInstance(project).findFile(targetFile))
          }
        )

        when {
          headers.isEmpty() -> showCannotNavigateNotification(project, anchor, point)

          headers.size == 1 -> navigateToHeader(targetFile, Objects.requireNonNull(ContainerUtil.getFirstItem(headers)))

          else -> showHeadersPopup(headers, point)
        }
      }

      true
    }
  }

  private fun showCannotNavigateNotification(project: Project, anchor: String, point: RelativePoint) {
    val balloonBuilder = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(
      MarkdownBundle.message("markdown.navigate.to.header.no.headers", anchor), MessageType.WARNING, null
    )
    val balloon = balloonBuilder.createBalloon()
    balloon.show(point, Balloon.Position.below)
    Disposer.register(project, balloon)
  }

  private fun navigateToHeader(targetFile: VirtualFile, item: PsiElement) {
    val editor = FileEditorManager.getInstance(item.project).getSelectedEditor(targetFile) ?: return
    val splitEditor = editor as MarkdownSplitEditor
    val oldAutoScrollPreview: Boolean = splitEditor.isAutoScrollPreview
    if (!oldAutoScrollPreview) splitEditor.isAutoScrollPreview = true
    PsiNavigateUtil.navigate(item)
    if (!oldAutoScrollPreview) splitEditor.isAutoScrollPreview = false
  }

  private fun showHeadersPopup(headers: Collection<PsiElement>, point: RelativePoint) {
    val headersPopup: ListPopupStep<*> =
      object : BaseListPopupStep<PsiElement?>(MarkdownBundle.message("markdown.navigate.to.header"), ArrayList(headers)) {
        fun getTextFor(value: PsiElement): String {
          val document = FileDocumentManager.getInstance().getDocument(value.containingFile.virtualFile)
          val name = value.containingFile.virtualFile.name
          return value.text + " (" + name + ":" + (Objects.requireNonNull(document)!!.getLineNumber(value.textOffset) + 1) + ")"
        }

        fun onChosen(selectedValue: PsiElement, finalChoice: Boolean): PopupStep<*>? {
          return doFinalStep { navigateToHeader(selectedValue.containingFile.virtualFile, selectedValue) }
        }
      }
    JBPopupFactory.getInstance().createListPopup(headersPopup).show(point)
  }

  private val LOG = Logger.getInstance(SafeOpener::class.java)

  private class InaccessibleURLOpenedException internal constructor(link: String?) : IllegalArgumentException(link)
}
