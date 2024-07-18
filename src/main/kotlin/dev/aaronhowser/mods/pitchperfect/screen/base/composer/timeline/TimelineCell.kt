package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import kotlin.random.Random

data class TimelineCell(
    val timeline: Timeline,
    val gridX: Int,
    val gridY: Int,
) {

    companion object {
        const val WIDTH = 9
        const val HEIGHT = 9
    }

    private val colorDefault = Random.nextInt(0x66000000, 0x66FFFFFF)
    private val colorHover = Random.nextInt(0x66000000, 0x66FFFFFF)

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

        val color = if (isMouseOver(pMouseX, pMouseY)) colorHover else colorDefault
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

        pGuiGraphics.renderComponentTooltip(
            timeline.font,
            components,
            pMouseX,
            pMouseY
        )
    }

    fun click(mouseX: Int, mouseY: Int, button: Int) {
        if (!isMouseOver(mouseX, mouseY)) return

        val adding = when (button) {
            0 -> true
            1 -> false
            else -> return
        }

        if (adding) {
            timeline.data.increment(delayX, pitchY)
        } else {
            timeline.data.decrement(delayX, pitchY)
        }

        return
    }

    private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in renderX..renderX + WIDTH && mouseY in renderY..renderY + HEIGHT
    }


}