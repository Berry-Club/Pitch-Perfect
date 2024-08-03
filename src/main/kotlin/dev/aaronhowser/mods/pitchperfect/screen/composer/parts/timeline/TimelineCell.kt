package dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.sounds.SoundManager
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.sounds.SoundEvent

data class TimelineCell(
    val timeline: Timeline,
    val gridX: Int,
    val gridY: Int,
) : AbstractWidget(
    getLeftPos(timeline, gridX),
    getTopPos(timeline, gridY),
    WIDTH,
    HEIGHT,
    Component.empty()
) {

    companion object {
        const val WIDTH = 7
        const val HEIGHT = 5

        private const val COLOR_DEFAULT_LIGHT = 0x66111111
        private const val COLOR_DEFAULT_DARK = 0x66444444

        private const val COLOR_HAS_INSTRUMENT = 0xFF333333.toInt()

        private fun getLeftPos(timeline: Timeline, gridX: Int) = timeline.leftPos + 1 + gridX * (WIDTH + 1)
        private fun getTopPos(timeline: Timeline, gridY: Int) = timeline.topPos + 1 + gridY * (HEIGHT + 2)
    }

    // Render position
    val renderLeft = getLeftPos(timeline, gridX)
    val renderRight = renderLeft + WIDTH
    val renderTop = getTopPos(timeline, gridY)
    val renderBottom = renderTop + HEIGHT

    // Timeline position
    val delay: Int
        get() = (gridX + timeline.horizontalScrollIndex) * Timeline.TICKS_PER_BEAT
    private val pitchInt: Int
        get() = Timeline.ROW_COUNT - gridY - 1
    val note: Note
        get() = Note.getFromPitch(pitchInt)

    val sounds: List<Holder<SoundEvent>>
        get() {
            val composerSong = timeline.composerScreen.composerSong ?: return emptyList()
            return composerSong.getSoundsAt(delay, pitchInt)
        }

    private val componentList: List<Component>
        get() {
            val components = mutableListOf<MutableComponent>()

            val delayComponent = ModLanguageProvider.Tooltip.DELAY
                .toComponent()
                .withStyle(ChatFormatting.GRAY)
                .append(
                    Component.literal("$delay").withStyle(ChatFormatting.WHITE)
                )

            val pitchComponent = ModLanguageProvider.Tooltip.PITCH
                .toComponent()
                .withStyle(ChatFormatting.GRAY)
                .append(
                    Component.literal(note.displayName).withColor(note.rgb)
                )

            components.add(delayComponent)
            components.add(pitchComponent)

            if (sounds.isNotEmpty()) {
                val listStartComponent = ModLanguageProvider.Tooltip.SOUNDS_LIST_START
                    .toComponent().withStyle(ChatFormatting.GRAY)
                components.add(listStartComponent)

                val countMap = sounds.groupingBy { it }.eachCount()
                for ((soundHolder, amount) in countMap) {
                    val instrument = ModItems.getFromSoundHolder(soundHolder)

                    val instrumentComponent = if (instrument != null) {
                        instrument.get().description as MutableComponent
                    } else {
                        Component.literal(soundHolder.key.toString())
                    }.withStyle(ChatFormatting.WHITE)

                    val component = if (amount > 1) {
                        Component.literal(" - ").withStyle(ChatFormatting.GRAY)
                            .append(instrumentComponent)
                            .append(
                                Component.literal(" x$amount").withStyle(ChatFormatting.WHITE)
                            )
                    } else {
                        Component.literal(" - ").append(instrumentComponent)
                    }.withStyle(ChatFormatting.GRAY)

                    components.add(component)
                }
            }

            return components
        }

    private val argb: Int
        get() {
            if (sounds.isEmpty()) return if (delay % 16 < 8) COLOR_DEFAULT_LIGHT else COLOR_DEFAULT_DARK

            val noteColor = Note.getFromPitch(pitchInt).withAlpha(0.8f)

            return noteColor
        }

    private val selectedInstrument: ScreenInstrument?
        get() = timeline.composerScreen.selectedInstrument

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        if (isMouseOver(pMouseX, pMouseY)) renderTooltip(pGuiGraphics, pMouseX, pMouseY)

        renderIndicator(pGuiGraphics)

        pGuiGraphics.fill(
            renderLeft,
            renderTop,
            renderRight,
            renderBottom,
            argb
        )
    }

    private fun renderIndicator(pGuiGraphics: GuiGraphics) {
        val selectedInstrument = selectedInstrument ?: return
        if (selectedInstrument.noteBlockInstrument.soundEvent !in sounds) return

        pGuiGraphics.renderOutline(
            renderLeft - 1,
            renderTop - 1,
            WIDTH + 2,
            HEIGHT + 2,
            COLOR_HAS_INSTRUMENT
        )
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.renderComponentTooltip(
            timeline.font,
            componentList,
            pMouseX,
            pMouseY
        )
    }

    override fun isValidClickButton(pButton: Int): Boolean {
        return pButton == 0 || pButton == 1
    }

    override fun playDownSound(pHandler: SoundManager) {
        // No sound
    }

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
        val leftClick = when (button) {
            0 -> true
            1 -> false
            else -> return
        }

        val soundHolder = selectedInstrument?.noteBlockInstrument?.soundEvent ?: return
        val soundString = Song.getSoundString(soundHolder)

        ModPacketHandler.messageServer(
            ClickComposerCellPacket(
                delay,
                pitchInt,
                leftClick,
                soundString,
                timeline.composerScreen.composerBlockEntity.blockPos
            )
        )

        if (leftClick) {
            timeline.composerScreen.minecraft.player?.playSound(
                soundHolder.value(),
                1f,
                pitchInt.map(0f, 24f, 0.5f, 2f)
            )
        }
    }

    private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in renderLeft..renderRight && mouseY in renderTop..renderBottom
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput)
    }


}