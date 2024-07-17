package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    companion object {
        private const val ROW_COUNT = 12
    }

    private val timelineTopPos by lazy { composerScreen.topPos + 85 }
    private val timelineLeftPos by lazy { composerScreen.leftPos + 84 }

    var scrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderNoteNames(pGuiGraphics)
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