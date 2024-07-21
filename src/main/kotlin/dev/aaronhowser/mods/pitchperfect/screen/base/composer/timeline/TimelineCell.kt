package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component

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
    private val delayX: Int
        get() = gridX + timeline.horizontalScrollIndex
    private val pitchY: Int
        get() = gridY + timeline.verticalScrollIndex


    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (isMouseOver(pMouseX, pMouseY)) renderTooltip(pGuiGraphics, pMouseX, pMouseY)

        val hasData = cellData != null

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

        components.add(Component.literal("Delay: $delayX"))
        components.add(Component.literal("Pitch: $pitchY"))

        if (cellData != null) {
            val instrumentCounts = cellData?.getAllCounts() ?: emptyMap()

            for ((instrument, count) in instrumentCounts) {
                components.add(Component.literal("${instrument.name}: $count"))
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
                delayX,
                pitchY,
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