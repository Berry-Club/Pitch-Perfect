package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class Timeline(
    val composerScreen: ComposerScreen,
    val font: Font
) {

    companion object {
        private const val ROW_COUNT = 25
    }

    val topPos by lazy { composerScreen.topPos + 60 }
    val leftPos by lazy { composerScreen.leftPos + 85 }

    var horizontalScrollIndex: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    fun init() {
        addTimelineCells()
    }

    private val timelineCells: MutableList<TimelineCell> = mutableListOf()

    private fun addTimelineCells() {
        if (timelineCells.isNotEmpty()) {
            PitchPerfect.LOGGER.error("Tried to add timeline cells when they already exist")
            return
        }

        for (yIndex in 0 until ROW_COUNT) {
            for (xIndex in 0 until 40) {
                timelineCells += TimelineCell(this, xIndex, yIndex)
            }
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderTimelineCells(pGuiGraphics, pMouseX, pMouseY)
        renderScrollIndex(pGuiGraphics)
    }

    private fun renderTimelineCells(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        for (cell in timelineCells) {
            cell.render(pGuiGraphics, pMouseX, pMouseY)
        }
    }

    private fun renderScrollIndex(pGuiGraphics: GuiGraphics) {
        pGuiGraphics.drawString(
            font,
            "$horizontalScrollIndex",
            leftPos,
            topPos - 20,
            0xFFFFFF
        )
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (composerScreen.selectedInstrument == null) return

        for (cell in timelineCells) {
            cell.click(pMouseX.toInt(), pMouseY.toInt(), pButton)
        }
    }

}