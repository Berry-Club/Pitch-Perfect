package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent

data class TimelineCell(
    val timeline: Timeline,
    val gridX: Int,
    val gridY: Int,
) : AbstractWidget(
    getLeftPos(timeline, gridX),
    getTopPos(timeline, gridY),
    WIDTH,
    HEIGHT,
    Component.empty()
) {

    companion object {
        const val WIDTH = 9
        const val HEIGHT = 5

        private const val COLOR_EMPTY = 0x66333333

        private fun getLeftPos(timeline: Timeline, gridX: Int) = timeline.leftPos + 1 + gridX * (WIDTH + 1)
        private fun getTopPos(timeline: Timeline, gridY: Int) = timeline.topPos + 1 + gridY * (HEIGHT + 2)
    }

    // Render position
    val renderLeft = getLeftPos(timeline, gridX)
    val renderRight = renderLeft + WIDTH
    val renderTop = getTopPos(timeline, gridY)
    val renderBottom = renderTop + HEIGHT

    // Timeline position
    val delay: Int
        get() = (gridX + timeline.horizontalScrollIndex) * Timeline.TICKS_PER_BEAT
    private val pitchInt: Int
        get() = Timeline.ROW_COUNT - gridY - 1
    val note: Note
        get() = Note.getFromPitch(pitchInt)

    private val soundStrings: List<String>
        get() {
            val songWip = timeline.composerScreen.songWip ?: return emptyList()
            return songWip.getSoundStringsAt(delay, pitchInt)
        }

    val sounds: List<Holder<SoundEvent>>
        get() {
            val songWip = timeline.composerScreen.songWip ?: return emptyList()
            return songWip.getSoundsAt(delay, pitchInt)
        }

    private val argb: Int
        get() {
            if (soundStrings.isEmpty()) return COLOR_EMPTY

            val noteColor = Note.getFromPitch(pitchInt).withAlpha(0.8f)

            return noteColor
        }

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
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
        components.add(Component.literal("Pitch: ${note.displayName}"))

        if (soundStrings.isNotEmpty()) {
            components.add(Component.literal("Sounds:"))
            for (soundString in soundStrings) {
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

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
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

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput)
    }


}