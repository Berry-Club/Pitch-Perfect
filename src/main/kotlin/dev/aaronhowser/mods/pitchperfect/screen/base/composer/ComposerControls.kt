package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ComposerPasteSongPacket
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

class ComposerControls(
    private val composerScreen: ComposerScreen,
    private val font: Font
) {

    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var copyButton: Button
    private lateinit var pasteButton: Button

    private lateinit var jumpToTickBox: EditBox

    fun init() {
        createWidgets()
    }

    private fun createWidgets() {
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

        copyButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19,
            16,
            16,
            Component.literal("Copy")
        ) {
            val song = composerScreen.songWip?.song ?: return@textButton

            composerScreen.minecraft.keyboardHandler.clipboard = song.toString()

            PitchPerfect.LOGGER.info("Copied song to clipboard!\n$song")
        }

        pasteButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19 + 19,
            16,
            16,
            Component.literal("Paste")
        ) {
            val clipboard = composerScreen.minecraft.keyboardHandler.clipboard
            val song = Song.fromString(clipboard)

            if (song == null) {
                composerScreen.minecraft.player?.sendSystemMessage(Component.literal("Failed to parse song:\n$clipboard"))
                return@textButton
            }

            ModPacketHandler.messageServer(
                ComposerPasteSongPacket(
                    song,
                    composerScreen.composerBlockEntity.blockPos
                )
            )

            PitchPerfect.LOGGER.info("Pasted song from clipboard!\n$song")
        }

        jumpToTickBox = EditBox(
            font,
            composerScreen.leftPos + 80,
            composerScreen.topPos + 40,
            30,
            10,
            Component.literal("Jump to tick")
        )

        jumpToTickBox.value = "000"
        jumpToTickBox.setEditable(true)
        jumpToTickBox.setMaxLength(5)

        jumpToTickBox.setResponder { newValue ->
            jumpToTickBox.value = newValue.filter { it.isDigit() }
        }

        composerScreen.addWidgets(playButton, stopButton, copyButton, pasteButton, jumpToTickBox)
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        playButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        stopButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        copyButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        pasteButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        jumpToTickBox.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (playButton.isHoveredOrFocused) playButton.onPress()
        if (stopButton.isHoveredOrFocused) stopButton.onPress()
        if (copyButton.isHoveredOrFocused) copyButton.onPress()
        if (pasteButton.isHoveredOrFocused) pasteButton.onPress()

        if (jumpToTickBox.isHoveredOrFocused) {
            jumpToTickBox.isFocused = true
        } else {
            jumpToTickBox.isFocused = false
        }
    }

    fun keyPressed(pKeyCode: Int, pScanCode: Int, pModifiers: Int) {
        jumpToTickBox.keyPressed(pKeyCode, pScanCode, pModifiers)
    }

}