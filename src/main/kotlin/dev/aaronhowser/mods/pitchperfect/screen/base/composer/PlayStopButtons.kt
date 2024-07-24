package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

class PlayStopButtons(
    private val composerScreen: ComposerScreen
) {

    private lateinit var playButton: Button
    private lateinit var stopButton: Button

    fun init() {
        addButtons()
    }

    private fun addButtons() {
        fun textButton(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            component: Component,
            onPress: (Button) -> Unit
        ): PlainTextButton {
            return PlainTextButton(
                x, y,
                width, height,
                component,
                onPress,
                composerScreen.font
            )
        }

        playButton = textButton(
            composerScreen.leftPos + 5 + 40,
            composerScreen.topPos + 5 + 19 + 19,
            16,
            16,
            Component.literal("Play")
        ) {
            composerScreen.timeline.timelineStepper.startPlaying()
        }

        stopButton = textButton(
            composerScreen.leftPos + 5 + 40,
            composerScreen.topPos + 5 + 19 + 19 + 19,
            16,
            16,
            Component.literal("Stop")
        ) {
            composerScreen.timeline.timelineStepper.stopPlaying()
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        playButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        stopButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (playButton.isMouseOver(pMouseX, pMouseY)) playButton.onPress()
        if (stopButton.isMouseOver(pMouseX, pMouseY)) stopButton.onPress()
    }

}