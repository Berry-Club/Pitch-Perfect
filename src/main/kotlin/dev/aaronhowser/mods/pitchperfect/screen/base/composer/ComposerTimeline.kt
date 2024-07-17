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

    private val timelineTopPos by lazy { composerScreen.topPos + BUFFER_SPACE + INSTRUMENT_BUTTON_SIZE + BUFFER_SPACE + 20 }

    var scrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderNoteNames(pGuiGraphics)
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


}