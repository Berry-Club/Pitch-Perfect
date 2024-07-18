package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.random.Random

@OnlyIn(Dist.CLIENT)
class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    companion object {
        private const val ROW_COUNT = 12
    }

    private val timelineTopPos by lazy { composerScreen.topPos + 84 }
    private val timelineLeftPos by lazy { composerScreen.leftPos + 85 }

    var scrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }

    fun init() {
        addNoteCells()
    }

    data class Cell(
        val x: Int,
        val y: Int,
        val instruments: List<ComposerScreen.Instrument>
    ) {

        companion object {
            const val WIDTH = 9
            const val HEIGHT = 9
        }

        private val colorDefault = Random.nextInt(0x66000000, 0x66FFFFFF)
        private val colorHover = Random.nextInt(0x66000000, 0x66FFFFFF)

        fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
            val color = if (isMouseOver(pMouseX, pMouseY)) colorHover else colorDefault
            pGuiGraphics.fill(x, y, x + WIDTH, y + HEIGHT, color)
        }

        private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
            return mouseX in x..x + WIDTH && mouseY in y..y + HEIGHT
        }
    }

    private var noteCells: List<Cell> = emptyList()
    private fun addNoteCells() {
        for (yIndex in 0 until ROW_COUNT) {
            for (xIndex in 0 until 40) {
                val x = timelineLeftPos + 1 + xIndex * (Cell.WIDTH + 1)
                val y = timelineTopPos + 1 + yIndex * (Cell.HEIGHT + 4)

                noteCells += Cell(x, y, emptyList())
            }
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderNoteCells(pGuiGraphics, pMouseX, pMouseY)
        renderNoteNames(pGuiGraphics)
    }

    private fun renderNoteCells(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        for (cell in noteCells) {
            cell.render(pGuiGraphics, pMouseX, pMouseY)
        }
    }

    private fun renderNoteNames(pGuiGraphics: GuiGraphics) {
        val x = timelineLeftPos - 20

        for (yIndex in 0 until ROW_COUNT) {
            val y = timelineTopPos + yIndex * 13

            val noteIndex = yIndex + scrollIndex
            val note = Note.entries.getOrNull(noteIndex)
            val noteString = note?.displayName ?: "YOU FUCKED UP"
            val noteColor = note?.color ?: 0xFFFFFF

            pGuiGraphics.drawString(
                Minecraft.getInstance().font,
                noteString,
                x,
                y,
                noteColor
            )
        }
    }


}