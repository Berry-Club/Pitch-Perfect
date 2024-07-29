package dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
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
    getTopPos(timeline),
    WIDTH,
    HEIGHT,
    Component.empty()
) {

    companion object {
        private const val WIDTH = ScreenTextures.Sprite.StepJump.WIDTH
        private const val HEIGHT = ScreenTextures.Sprite.StepJump.HEIGHT

        private fun getLeftPos(timeline: Timeline, gridX: Int): Int = timeline.leftPos + 1 + gridX * (WIDTH + 1)
        private fun getTopPos(timeline: Timeline): Int = timeline.topPos - 4
    }

    val delay: Int
        get() = (gridX + timeline.horizontalScrollIndex) * Timeline.TICKS_PER_BEAT

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        pGuiGraphics.blitSprite(
            ScreenTextures.Sprite.StepJump.STEP_JUMPER,
            ScreenTextures.Sprite.StepJump.CANVAS_SIZE,
            ScreenTextures.Sprite.StepJump.CANVAS_SIZE,
            0,
            0,
            this.x,
            this.y,
            ScreenTextures.Sprite.StepJump.WIDTH,
            ScreenTextures.Sprite.StepJump.HEIGHT
        )

        if (isMouseOver(pMouseX.toDouble(), pMouseY.toDouble())) {
            renderTooltip(pGuiGraphics, pMouseX, pMouseY)
        }
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput)
    }

    override fun isValidClickButton(pButton: Int): Boolean {
        return pButton == 0
    }

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
        timeline.timelineStepper.setDelay(delay, fromEditBox = false)
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        val components = mutableListOf<Component>()

        components.add(ModLanguageProvider.Tooltip.JUMP_TO_BEAT_SPECIFIC.toComponent(delay))

        pGuiGraphics.renderComponentTooltip(
            timeline.font,
            components,
            pMouseX,
            pMouseY
        )
    }

}