package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.BUFFER_SPACE
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen.Companion.INSTRUMENT_BUTTON_SIZE
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    companion object {
        private const val ROW_COUNT = 12
    }

    data class TimelineButton(
        var button: Button? = null,
        var instruments: MutableList<ComposerScreen.Instrument> = mutableListOf()
    )

    val timelineButtons: MutableMap<Pair<Int, Int>, TimelineButton> = mutableMapOf()
    private val timelineTopPos by lazy { composerScreen.topPos + BUFFER_SPACE + INSTRUMENT_BUTTON_SIZE + BUFFER_SPACE + 20 }

    var scrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderNoteNames(pGuiGraphics)
    }

    fun addButtons() {
        addTimelineButtons()
    }

    private fun addTimelineButtons() {
        val width = 16
        val height = 16

        for (yIndex in 0 until 24) {
            for (xIndex in 0 until 24) {
                var timelineButton = TimelineButton()

                val button = Button.Builder(Component.empty()) { button ->
                    val selectedInstrument = composerScreen.selectedInstrument
                    if (selectedInstrument != null) {
                        if (timelineButton.instruments.contains(selectedInstrument)) {
                            timelineButton.instruments.remove(selectedInstrument)
                        } else {
                            timelineButton.instruments.add(selectedInstrument)
                        }
                    }

                    val component =
                        Component.empty().append(timelineButton.instruments.map { it.name }.joinToString("\n"))
                    button.tooltip = Tooltip.create(component)
                }
                    .size(width, height)
                    .build()

                timelineButton = TimelineButton(button)
                timelineButtons[Pair(xIndex, yIndex)] = timelineButton
            }
        }
    }

    private fun renderNoteNames(pGuiGraphics: GuiGraphics) {
        val x = composerScreen.leftPos + 5 + 4

        for (yIndex in 0 until ROW_COUNT) {
            val y = timelineTopPos + 3 + yIndex * 16

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

    private fun renderButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderNoteButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    private fun renderNoteButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        val width = 16
        val height = 16

        for ((pos, timelineButton) in timelineButtons) {

            val (gridX, gridY) = pos

            if (gridY !in scrollIndex until scrollIndex + ROW_COUNT) {
                continue
            }

            val x = composerScreen.leftPos + 5 + 16 + gridX * width
            val y = timelineTopPos + 3 + (gridY - scrollIndex) * height

            timelineButton.button?.apply {
                this.x = x
                this.y = y

                render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
            }
        }
    }

}