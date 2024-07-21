package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent

data class TimelineCell(
    val timeline: Timeline,
    val gridX: Int,
    val gridY: Int,
) {

    companion object {
        const val WIDTH = 9
        const val HEIGHT = 9

        private const val COLOR_EMPTY = 0x66333333
        private const val COLOR_NOT_EMPTY = 0x66FFFFFF
    }

    // Render position
    private val renderX = timeline.leftPos + 1 + gridX * (WIDTH + 1)
    private val renderY = timeline.topPos + 1 + gridY * (HEIGHT + 4)

    // Timeline position
    private val delay: Int
        get() = gridX + timeline.horizontalScrollIndex
    private val pitch: Int
        get() = gridY + timeline.verticalScrollIndex

    val sounds: List<Holder<SoundEvent>>
        get() {
            val timeline = timeline.composerScreen.composerBlockEntity.composerTimeline ?: return emptyList()
            return timeline.getSoundsAt(delay, pitch)
        }


    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (isMouseOver(pMouseX, pMouseY)) renderTooltip(pGuiGraphics, pMouseX, pMouseY)

        val hasData = sounds.isNotEmpty()

        val color = if (hasData) COLOR_NOT_EMPTY else COLOR_EMPTY

        pGuiGraphics.fill(
            renderX,
            renderY,
            renderX + WIDTH,
            renderY + HEIGHT,
            color
        )
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        val components = mutableListOf<Component>()

        components.add(Component.literal("Delay: $delay"))
        components.add(Component.literal("Pitch: $pitch"))

        if (sounds.isNotEmpty()) {
            components.add(Component.literal("Sounds:"))
            for (sound in sounds) {
                components.add(Component.literal("  - ${sound.value().location}"))
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

        ModPacketHandler.messageServer(
            ClickComposerCellPacket(
                delay,
                pitch,
                leftClick,
                Holder.direct(timeline.composerScreen.selectedInstrument!!.instrumentItem.instrument),
                timeline.composerScreen.composerBlockEntity.blockPos
            )
        )
    }

    private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in renderX..renderX + WIDTH && mouseY in renderY..renderY + HEIGHT
    }


}