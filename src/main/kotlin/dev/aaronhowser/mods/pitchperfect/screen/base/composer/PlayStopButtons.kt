package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

class PlayStopButtons(
    private val composerScreen: ComposerScreen,
    private val font: Font
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
                font
            )
        }

        playButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19,
            16,
            16,
            Component.literal("Copy")
        ) {
            val song = composerScreen.composerBlockEntity.songWip?.song ?: return@textButton
        }

        stopButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19 + 19,
            16,
            16,
            Component.literal("Paste")
        ) {
            val song = composerScreen.composerBlockEntity.songWip?.song ?: return@textButton
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {

    }

}