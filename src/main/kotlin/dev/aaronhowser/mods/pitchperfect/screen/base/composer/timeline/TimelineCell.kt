package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

data class TimelineCell(
    val timeline: Timeline,
    val gridX: Int,
    val gridY: Int,
) {

    companion object {
        const val WIDTH = 9
        const val HEIGHT = 5

        private const val COLOR_EMPTY = 0x66333333
        private const val COLOR_NOT_EMPTY = 0x66FFFFFF
    }

    // Render position
    private val renderLeft = timeline.leftPos + 1 + gridX * (WIDTH + 1)
    private val renderRight = renderLeft + WIDTH
    private val renderTop = timeline.topPos + 1 + gridY * (HEIGHT + 2)
    private val renderBottom = renderTop + HEIGHT

    // Timeline position
    private val delay: Int
        get() = (gridX + timeline.horizontalScrollIndex) * 2
    private val pitchInt: Int
        get() = gridY
    private val noteName: String
        get() = Note.getFromPitch(pitchInt).displayName

    val sounds: List<String>
        get() {
            val songWip = timeline.composerScreen.composerBlockEntity.songWip ?: return emptyList()
            return songWip.getSoundStringsAt(delay, pitchInt)
        }

    private val argb: Int
        get() {
            if (sounds.isEmpty()) return COLOR_EMPTY

            val noteColor = Note.getFromPitch(pitchInt).withAlpha(0.8f)

            return noteColor
        }


    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (isMouseOver(pMouseX, pMouseY)) renderTooltip(pGuiGraphics, pMouseX, pMouseY)

        pGuiGraphics.fill(
            renderLeft,
            renderTop,
            renderRight,
            renderBottom,
            argb
        )
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        val components = mutableListOf<Component>()

        components.add(Component.literal("Delay: $delay"))
        components.add(Component.literal("Pitch: $noteName"))

        if (sounds.isNotEmpty()) {
            components.add(Component.literal("Sounds:"))
            for (soundString in sounds) {
                components.add(Component.literal("  - $soundString"))
            }
        }

        pGuiGraphics.renderComponentTooltip(
            timeline.font,
            components,
            pMouseX,
            pMouseY
        )
    }

    fun click(mouseX: Int, mouseY: Int, button: Int) {
        if (!isMouseOver(mouseX, mouseY)) return

        val leftClick = when (button) {
            0 -> true
            1 -> false
            else -> return
        }

        val soundHolder = timeline.composerScreen.selectedInstrument?.noteBlockInstrument?.soundEvent ?: return
        val soundString = Song.getSoundString(soundHolder)

        ModPacketHandler.messageServer(
            ClickComposerCellPacket(
                delay,
                pitchInt,
                leftClick,
                soundString,
                timeline.composerScreen.composerBlockEntity.blockPos
            )
        )

        if (leftClick) {
            timeline.composerScreen.minecraft.player?.playSound(
                soundHolder.value(),
                1f,
                pitchInt.map(0f, 24f, 0.5f, 2f)
            )
        }
    }

    private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in renderLeft..renderRight && mouseY in renderTop..renderBottom
    }


}