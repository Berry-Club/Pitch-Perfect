package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ComposerPasteSongPacket
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline.Timeline
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

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

        fun addIconButton(
            x: Int, y: Int,
            width: Int, height: Int,
            image: ResourceLocation,
            component: Component = Component.empty(),
            onPress: (Button) -> Unit = {}
        ): SpriteIconButton {
            val button = SpriteIconButton
                .builder(component, onPress, true)
                .sprite(image, 16, 16)
                .size(width, height)
                .build()
                .apply {
                    this.x = x
                    this.y = y
                }

            return button
        }

        jumpToBeatBox = EditBox(
            font,
            composerScreen.timeline.leftPos,
            composerScreen.timeline.topPos - 20,
            72,
            14,
            Component.literal("Jump to beat")
        ).apply {
            setMaxLength(9)
            setHint(Component.literal("Jump to beat"))

            setResponder { newValue ->
                setBoxValue(newValue)
            }
        }

        playButton = addIconButton(
            composerScreen.timeline.leftPos + jumpToBeatBox.width + 5,
            composerScreen.timeline.topPos - 22,
            16,
            16,
            ScreenTextures.Sprite.Instrument.BIT,
            Component.literal("Play")
        ) { startPlaying() }

        stopButton = addIconButton(
            composerScreen.timeline.leftPos + jumpToBeatBox.width + 5 + 16 + 5,
            composerScreen.timeline.topPos - 22,
            16,
            16,
            ScreenTextures.Sprite.Instrument.BIT,
            Component.literal("Stop")
        ) { stopPlaying() }

        copyButton = addIconButton(
            composerScreen.timeline.leftPos + jumpToBeatBox.width + 5 + (16 + 5) * 2,
            composerScreen.timeline.topPos - 22,
            16,
            16,
            ScreenTextures.Sprite.Instrument.BIT,
            Component.literal("Copy")
        ) { copySong() }

        pasteButton = addIconButton(
            composerScreen.timeline.leftPos + jumpToBeatBox.width + 5 + (16 + 5) * 3,
            composerScreen.timeline.topPos - 22,
            16,
            16,
            ScreenTextures.Sprite.Instrument.BIT,
            Component.literal("Paste")
        ) { pasteSong() }

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