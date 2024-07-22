package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import net.minecraft.client.gui.GuiGraphics

class TimelineStepper(
    private val timeline: Timeline
) {

    private var currentDelay = 0
        set(value) {
            field = value.coerceIn(0, timeline.lastBeatDelay)
            setCellsAtBeat()
        }

    private var cellsAtBeat: List<TimelineCell> = listOf()

    fun setCellsAtBeat() {
        cellsAtBeat = timeline.timelineCells.filter { it.delay == currentDelay }
    }

    fun init() {
        setCellsAtBeat()
    }

    fun render(pGuiGraphics: GuiGraphics) {
        if (cellsAtBeat.isEmpty()) return

        val topCellY = cellsAtBeat.minOf { it.renderTop }
        val bottomCellY = cellsAtBeat.maxOf { it.renderBottom }
        val left = cellsAtBeat.first().renderLeft
        val right = cellsAtBeat.last().renderRight

        pGuiGraphics.fill(
            left - 1,
            topCellY - 1,
            right + 1,
            bottomCellY + 1,
            0x66FF6666
        )

    }

}