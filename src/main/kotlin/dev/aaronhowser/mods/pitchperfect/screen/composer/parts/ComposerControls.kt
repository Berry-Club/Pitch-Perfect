package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ComposerPasteSongPacket
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline.Timeline
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

class ComposerControls(
    private val composerScreen: ComposerScreen,
    private val font: Font
) {

    //TODO: Drag clicking

    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var copyButton: Button
    private lateinit var pasteButton: Button

    lateinit var jumpToBeatBox: EditBox

    fun init() {
        createWidgets()
    }

    fun copySong() {
        val song = composerScreen.songWip?.song ?: return

        composerScreen.minecraft.keyboardHandler.clipboard = song.toString()

        PitchPerfect.LOGGER.info("Copied song to clipboard!")
        PitchPerfect.LOGGER.info(song.toString())
    }

    fun pasteSong() {
        val clipboard = composerScreen.minecraft.keyboardHandler.clipboard
        val song = Song.fromString(clipboard)

        if (song == null) {
            composerScreen.minecraft.player?.sendSystemMessage(Component.literal("Failed to parse song:\n$clipboard"))
            return
        }

        ModPacketHandler.messageServer(
            ComposerPasteSongPacket(
                song,
                composerScreen.composerBlockEntity.blockPos
            )
        )

        PitchPerfect.LOGGER.info("Pasted song from clipboard!")
        PitchPerfect.LOGGER.info(song.toString())
    }

    fun startPlaying() {
        composerScreen.timeline.timelineStepper.startPlaying()
    }

    fun stopPlaying() {
        composerScreen.timeline.timelineStepper.stopPlaying()
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
        ) { startPlaying() }

        stopButton = textButton(
            composerScreen.leftPos + 5 + 40,
            composerScreen.topPos + 5 + 19 + 19 + 19,
            16,
            16,
            Component.literal("Stop")
        ) { stopPlaying() }

        copyButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19,
            16,
            16,
            Component.literal("Copy")
        ) { copySong() }

        pasteButton = textButton(
            composerScreen.leftPos + 5,
            composerScreen.topPos + 5 + 19 + 19 + 19,
            16,
            16,
            Component.literal("Paste")
        ) { pasteSong() }

        jumpToBeatBox = EditBox(
            font,
            composerScreen.leftPos + 86,
            composerScreen.topPos + 39,
            72,
            14,
            Component.literal("Jump to beat")
        )

        jumpToBeatBox.apply {
            setMaxLength(9)
            setHint(Component.literal("Jump to beat"))

            setResponder { newValue ->
                setBoxValue(newValue)
            }
        }

        composerScreen.addRenderableWidgets(playButton, stopButton, copyButton, pasteButton, jumpToBeatBox)
    }

    private var changedThisTick = false
    fun setBoxValue(string: String, fromStepper: Boolean = false) {
        if (changedThisTick) return
        changedThisTick = true

        val theString = string.filter { it.isDigit() }
        jumpToBeatBox.value = if (theString == "0") "" else theString

        changedThisTick = false

        if (fromStepper) return

        val intValue = string.toIntOrNull() ?: 0
        val newDelay = intValue - (intValue % Timeline.TICKS_PER_BEAT)
        composerScreen.timeline.timelineStepper.setDelay(newDelay, fromEditBox = true)

        val idealScrollIndex = newDelay / Timeline.TICKS_PER_BEAT - Timeline.COLUMN_COUNT / 2 + 1
        composerScreen.timeline.horizontalScrollIndex = idealScrollIndex
    }

}