package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.random.Random

@OnlyIn(Dist.CLIENT)
class ComposerTimeline(
    private val composerScreen: ComposerScreen
) {

    companion object {
        private const val ROW_COUNT = 12
    }

    private val topPos by lazy { composerScreen.topPos + 84 }
    private val leftPos by lazy { composerScreen.leftPos + 85 }

    var verticalScrollIndex: Int = 0
        set(value) {
            field = value.coerceIn(0, Note.entries.size - ROW_COUNT)
        }
    var horizontalScrollIndex: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    fun init() {
        addNoteCells()
    }

    private var noteCells: List<Cell> = emptyList()

    data class Cell(
        val timeline: ComposerTimeline,
        val gridX: Int,
        val gridY: Int,
        val instruments: MutableMap<ComposerScreen.Instrument, Int>
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

            for ((instrument, count) in instruments) {
                components.add(Component.literal("${instrument.name}: $count"))
            }

            pGuiGraphics.renderComponentTooltip(
                Minecraft.getInstance().font,
                components,
                pMouseX,
                pMouseY
            )
        }

        fun click(mouseX: Int, mouseY: Int, button: Int) {
            if (!isMouseOver(mouseX, mouseY)) return

            val instrument = timeline.composerScreen.selectedInstrument ?: return

            val current = instruments.getOrDefault(instrument, 0)
            instruments[instrument] = (if (button == 0) current + 1 else current - 1).coerceAtLeast(0)
            if (instruments[instrument] == 0) instruments.remove(instrument)
        }

        private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
            return mouseX in renderX..renderX + WIDTH && mouseY in renderY..renderY + HEIGHT
        }

    }

    private fun addNoteCells() {
        for (yIndex in 0 until ROW_COUNT) {
            for (xIndex in 0 until 40) {
                noteCells += Cell(this, xIndex, yIndex, mutableMapOf())
            }
        }
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderNoteCells(pGuiGraphics, pMouseX, pMouseY)
        renderNoteNames(pGuiGraphics)
        renderScrollIndex(pGuiGraphics)
    }

    private fun renderNoteCells(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        for (cell in noteCells) {
            cell.render(pGuiGraphics, pMouseX, pMouseY)
        }
    }

    private fun renderNoteNames(pGuiGraphics: GuiGraphics) {
        val x = leftPos - 20

        for (yIndex in 0 until ROW_COUNT) {
            val y = topPos + yIndex * 13

            val noteIndex = yIndex + verticalScrollIndex
            val note = Note.entries.getOrNull(noteIndex)
            val noteString = note?.displayName ?: "YOU FUCKED UP"
            val noteColor = note?.color ?: 0xFFFFFF

            pGuiGraphics.drawString(
                Minecraft.getInstance().font,
                noteString,
                x,
                y,
                noteColor
            )
        }
    }

    private fun renderScrollIndex(pGuiGraphics: GuiGraphics) {
        pGuiGraphics.drawString(
            Minecraft.getInstance().font,
            "$horizontalScrollIndex $verticalScrollIndex",
            leftPos,
            topPos - 20,
            0xFFFFFF
        )
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (composerScreen.selectedInstrument == null) return

        for (cell in noteCells) {
            cell.click(pMouseX.toInt(), pMouseY.toInt(), pButton)
        }

    }

}