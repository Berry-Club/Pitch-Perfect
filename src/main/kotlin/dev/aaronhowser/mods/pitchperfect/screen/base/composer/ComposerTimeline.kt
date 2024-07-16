package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.BUFFER_SPACE
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.INSTRUMENT_BUTTON_SIZE
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    companion object {
        private const val ROW_COUNT = 12
    }

    val timelineButtons: MutableMap<Pair<Int, Int>, Button> = mutableMapOf()
    private val timelineTopPos by lazy { composerScreen.topPos + BUFFER_SPACE + INSTRUMENT_BUTTON_SIZE + BUFFER_SPACE + 20 }

    lateinit var scrollUpButton: Button
    lateinit var scrollDownButton: Button
    private var scrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderNoteNames(pGuiGraphics)
    }

    fun addButtons() {
        addTimelineButtons()
        addScrollButtons()
    }

    private fun addScrollButtons() {
        val width = 16
        val height = 16

        val x = composerScreen.leftPos + 5
        val y = timelineTopPos - 18

        scrollUpButton = Button.Builder(Component.empty()) { scrollIndex-- }
            .size(width, height)
            .build()
            .apply {
                this.x = x
                this.y = y
            }

        scrollDownButton = Button.Builder(Component.empty()) { scrollIndex++ }
            .size(width, height)
            .build()
            .apply {
                this.x = x + 18
                this.y = y
            }

    }

    private fun addTimelineButtons() {
        val width = 16
        val height = 16

        for (yIndex in 0 until ROW_COUNT) {
            for (xIndex in 0 until 24) {

                val button = Button.Builder(Component.empty()) {}
                    .size(width, height)
                    .build()

                this.timelineButtons[Pair(xIndex, yIndex)] = button
            }
        }
    }

    private fun renderNoteNames(pGuiGraphics: GuiGraphics) {
        val x = composerScreen.leftPos + 5 + 4

        for (yIndex in 0 until ROW_COUNT) {
            val y = timelineTopPos + 3 + yIndex * 16

            val noteIndex = yIndex + scrollIndex
            val noteString = Note.entries.getOrNull(noteIndex)?.displayName ?: "YOU FUCKED UP"

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
        renderNoteButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderScrollButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    // TODO: Cache this instead of iterating every frame(?)
    private fun renderNoteButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
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

    private fun renderScrollButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        scrollUpButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        scrollDownButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

}