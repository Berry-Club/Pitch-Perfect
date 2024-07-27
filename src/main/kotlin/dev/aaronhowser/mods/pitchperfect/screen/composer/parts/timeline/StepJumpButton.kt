package dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline

import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class StepJumpButton(
    val timeline: Timeline,
    val gridX: Int,
) : AbstractWidget(
    getLeftPos(timeline, gridX),
    0,
    WIDTH,
    HEIGHT,
    Component.empty()
) {

    companion object {
        private const val WIDTH = ScreenTextures.Background.Composer.WIDTH
        private const val HEIGHT = ScreenTextures.Background.Composer.HEIGHT

        private fun getLeftPos(timeline: Timeline, gridX: Int) = timeline.leftPos + 1 + gridX * (WIDTH + 1)
        private fun getTopPos(timeline: Timeline, gridY: Int) = timeline.topPos + 1 + gridY * (HEIGHT + 2)
    }

    val delay: Int
        get() = (gridX + timeline.horizontalScrollIndex) * Timeline.TICKS_PER_BEAT

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        pGuiGraphics.blitSprite(
            ScreenTextures.Sprite.STEP_JUMPER,
            16,
            16,
            0,
            0,
            timeline.composerScreen.leftPos + getLeftPos(timeline, gridX),
            timeline.composerScreen.topPos + 1,
            9,
            4
        )
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput)
    }


}