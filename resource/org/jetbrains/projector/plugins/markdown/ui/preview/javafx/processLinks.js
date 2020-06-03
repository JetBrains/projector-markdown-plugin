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
if (window.__IntelliJTools === undefined) {
  window.__IntelliJTools = {}
}

(function () {
  var openInExternalBrowser = function (href) {
    try {
      window.JavaPanelBridge.openInExternalBrowser(href);
    }
    finally {
    }
  };

  window.__IntelliJTools.processClick = function (link) {
    if (!link.hasAttribute('href')) {
      return false;
    }

    var href = link.getAttribute('href');
    if (href[0] === '#') {
      var elementId = href.substring(1);
      var elementById = window.document.getElementById(elementId);
      if (elementById) {
        elementById.scrollIntoViewIfNeeded();
      }
    }
    else {
      openInExternalBrowser(link.href);
    }

    return false;
  };

  window.document.onclick = function (e) {
    var target = e.target;
    while (target && target.tagName !== 'A') {
      target = target.parentNode
    }

    if (!target) {
      return true;
    }

    if (target.tagName === 'A' && target.hasAttribute('href')) {
      e.stopPropagation();
      return window.__IntelliJTools.processClick(target)
    }
  }

})();
