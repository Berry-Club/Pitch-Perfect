package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.BUFFER_SPACE
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.INSTRUMENT_BUTTON_SIZE
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    private val timelineButtons: MutableMap<Pair<Int, Int>, Button> = mutableMapOf()

    private val timelineTopPos by lazy { composerScreen.topPos + BUFFER_SPACE + INSTRUMENT_BUTTON_SIZE + BUFFER_SPACE + 20 }

    fun addTimelineButtons() {
        val width = 16
        val height = 16

        for (yIndex in 0 until 24) {
            for (xIndex in 0 until 24) {

                val button = Button.Builder(Component.empty()) {}
                    .size(width, height)
                    .build()

                this.timelineButtons[Pair(xIndex, yIndex)] = button
            }
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderNoteNames(pGuiGraphics)
    }

    private fun renderNoteNames(pGuiGraphics: GuiGraphics) {
        val x = composerScreen.leftPos + 5 + 4

        for (yIndex in 0 until 8) {
            val y = timelineTopPos + 3 + yIndex * 16

            val noteString = when (yIndex) {
                0 -> "C"
                1 -> "D"
                2 -> "E"
                3 -> "F"
                4 -> "G"
                5 -> "A"
                6 -> "B"
                7 -> "C"
                else -> "X"
            }

            pGuiGraphics.drawString(
                Minecraft.getInstance().font,
                noteString,
                x,
                y,
                0xFFFFFF
            )
        }
    }

    private fun renderButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

        val width = 16
        val height = 16

        for ((pos, button) in timelineButtons) {

            val x = composerScreen.leftPos + 5 + 16 + pos.first * width
            val y = timelineTopPos + 3 + pos.second * height

            button.apply {
                this.x = x
                this.y = y

                render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
            }
        }

    }

}