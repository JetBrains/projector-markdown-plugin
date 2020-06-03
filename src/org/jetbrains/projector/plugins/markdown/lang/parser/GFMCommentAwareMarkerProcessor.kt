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

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.commonmark.CommonMarkMarkerProcessor
import org.intellij.markdown.flavours.gfm.GFMConstraints
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider
import org.intellij.markdown.parser.LookaheadText
import org.intellij.markdown.parser.MarkerProcessor
import org.intellij.markdown.parser.MarkerProcessorFactory
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.constraints.MarkdownConstraints
import org.intellij.markdown.parser.markerblocks.MarkerBlockProvider
import org.intellij.markdown.parser.markerblocks.providers.AtxHeaderProvider
import org.intellij.markdown.parser.markerblocks.providers.LinkReferenceDefinitionProvider
import org.intellij.markdown.parser.sequentialparsers.SequentialParser

class GFMCommentAwareMarkerProcessor(productionHolder: ProductionHolder, constraintsBase: MarkdownConstraints) :
  CommonMarkMarkerProcessor(productionHolder, constraintsBase) {

  private val markerBlockProviders = super.getMarkerBlockProviders()
    .filterNot { it is AtxHeaderProvider }
    .filterNot { it is LinkReferenceDefinitionProvider }
    .plus(
      listOf(
        GitHubTableMarkerProvider(),
        AtxHeaderProvider(false),
        CommentAwareLinkReferenceDefinitionProvider()
      )
    )

  override fun populateConstraintsTokens(
    pos: LookaheadText.Position,
    constraints: MarkdownConstraints,
    productionHolder: ProductionHolder
  ) {
    if (constraints !is GFMConstraints || !constraints.hasCheckbox()) {
      super.populateConstraintsTokens(pos, constraints, productionHolder)
      return
    }

    val line = pos.currentLine
    var offset = pos.offsetInCurrentLine
    while (offset < line.length && line[offset] != '[') {
      offset++
    }
    if (offset == line.length) {
      super.populateConstraintsTokens(pos, constraints, productionHolder)
      return
    }

    val type = when (constraints.getLastType()) {
      '>' ->
        MarkdownTokenTypes.BLOCK_QUOTE
      '.', ')' ->
        MarkdownTokenTypes.LIST_NUMBER
      else ->
        MarkdownTokenTypes.LIST_BULLET
    }
    val middleOffset = pos.offset - pos.offsetInCurrentLine + offset
    val endOffset = Math.min(
      pos.offset - pos.offsetInCurrentLine + constraints.getCharsEaten(pos.currentLine),
      pos.nextLineOrEofOffset
    )

    productionHolder.addProduction(
      listOf(
        SequentialParser.Node(pos.offset..middleOffset, type),
        SequentialParser.Node(middleOffset..endOffset, GFMTokenTypes.CHECK_BOX)
      )
    )
  }

  override fun getMarkerBlockProviders(): List<MarkerBlockProvider<StateInfo>> {
    return markerBlockProviders
  }

  object Factory : MarkerProcessorFactory {
    override fun createMarkerProcessor(productionHolder: ProductionHolder): MarkerProcessor<*> {
      return GFMCommentAwareMarkerProcessor(productionHolder, GFMConstraints.BASE)
    }
  }
}
