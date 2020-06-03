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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarkdownAccessor {

  private static ImageRefreshFixAccessor ourImageRefreshFixAccessor;
  private static SafeOpenerAccessor ourSafeOpenerAccessor;

  @NotNull
  public static ImageRefreshFixAccessor getImageRefreshFixAccessor() {
    if (ourImageRefreshFixAccessor == null) {
      try {
        Class.forName(MarkdownAccessor.class.getPackage().getName() + ".javafx.ImageRefreshFix", true,
                      MarkdownAccessor.class.getClassLoader());
      }
      catch (ClassNotFoundException e) {
        ourImageRefreshFixAccessor = new ImageRefreshFixAccessor() {
          @Override
          public String setStamps(@NotNull String html) {
            return html;
          }
        };
      }
    }
    return ourImageRefreshFixAccessor;
  }

  public static void setImageRefreshFixAccessor(@NotNull ImageRefreshFixAccessor accessor) {
    ourImageRefreshFixAccessor = accessor;
  }

  @NotNull
  public static SafeOpenerAccessor getSafeOpenerAccessor() {
    if (ourSafeOpenerAccessor == null) {
      try {
        Class.forName(MarkdownAccessor.class.getPackage().getName() + ".javafx.SafeOpener", true, MarkdownAccessor.class.getClassLoader());
      }
      catch (ClassNotFoundException e) {
        ourSafeOpenerAccessor = new SafeOpenerAccessor() {
          @Override
          public void openLink(@NotNull String link) {
          }

          @Override
          public boolean isSafeExtension(@Nullable String path) {
            return true;
          }
        };
      }
    }
    return ourSafeOpenerAccessor;
  }

  public static void setSafeOpenerAccessor(@NotNull SafeOpenerAccessor accessor) {
    ourSafeOpenerAccessor = accessor;
  }

  public interface ImageRefreshFixAccessor {
    String setStamps(@NotNull String html);
  }

  public interface SafeOpenerAccessor {
    void openLink(@NotNull String link);

    boolean isSafeExtension(@Nullable String path);
  }
}
