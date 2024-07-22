package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.PasteComposerSongPacket
import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

class CopyPasteButtons(
    private val composerScreen: ComposerScreen,
    private val font: Font
) {

    private lateinit var copyButton: Button
    private lateinit var pasteButton: Button

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

        copyButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19,
            16,
            16,
            Component.literal("Copy")
        ) {
            val song = composerScreen.composerBlockEntity.songWip?.song ?: return@textButton

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
                PasteComposerSongPacket(
                    song,
                    composerScreen.composerBlockEntity.blockPos
                )
            )

            PitchPerfect.LOGGER.info("Pasted song from clipboard!\n$song")
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    private fun renderButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        copyButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        pasteButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (copyButton.isHoveredOrFocused) copyButton.onPress()
        if (pasteButton.isHoveredOrFocused) pasteButton.onPress()
    }

}