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
package org.jetbrains.projector.plugins.markdown.lang.parser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.parser.LookaheadText
import org.intellij.markdown.parser.MarkerProcessor
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.constraints.MarkdownConstraints
import org.intellij.markdown.parser.markerblocks.MarkerBlock
import org.intellij.markdown.parser.markerblocks.MarkerBlockProvider
import org.intellij.markdown.parser.markerblocks.impl.LinkReferenceDefinitionMarkerBlock
import org.intellij.markdown.parser.markerblocks.providers.LinkReferenceDefinitionProvider
import org.intellij.markdown.parser.markerblocks.providers.LinkReferenceDefinitionProvider.Companion.addToRangeAndWiden
import org.intellij.markdown.parser.markerblocks.providers.LinkReferenceDefinitionProvider.Companion.isEndOfLine
import org.intellij.markdown.parser.sequentialparsers.SequentialParser

class CommentAwareLinkReferenceDefinitionProvider : MarkerBlockProvider<MarkerProcessor.StateInfo> {
  override fun createMarkerBlocks(
    pos: LookaheadText.Position,
    productionHolder: ProductionHolder,
    stateInfo: MarkerProcessor.StateInfo
  ): List<MarkerBlock> {

    if (!MarkerBlockProvider.isStartOfLineWithConstraints(pos, stateInfo.currentConstraints)) {
      return emptyList()
    }

    val matchResult = LinkReferenceDefinitionProvider.matchLinkDefinition(pos.textFromPosition) ?: return emptyList()
    for ((i, range) in matchResult.withIndex()) {
      productionHolder.addProduction(
        listOf(
          SequentialParser.Node(
            addToRangeAndWiden(range, pos.offset), when (i) {
            0 -> MarkdownElementTypes.LINK_LABEL
            1 -> MarkdownElementTypes.LINK_DESTINATION
            2 ->
              if (pos.currentLineFromPosition.startsWith("[//]: #")) {
                org.jetbrains.projector.plugins.markdown.lang.MarkdownElementTypes.COMMENT
              }
              else {
                MarkdownElementTypes.LINK_TITLE
              }
            else -> throw AssertionError("There are no more than three groups in this regex")
          }
          )
        )
      )
    }

    val matchLength = matchResult.last().endInclusive + 1
    val endPosition = pos.nextPosition(matchLength)

    if (endPosition != null && !isEndOfLine(endPosition)) {
      return emptyList()
    }
    return listOf(
      LinkReferenceDefinitionMarkerBlock(
        stateInfo.currentConstraints, productionHolder.mark(),
        pos.offset + matchLength
      )
    )
  }

  override fun interruptsParagraph(pos: LookaheadText.Position, constraints: MarkdownConstraints): Boolean {
    return false
  }
}
