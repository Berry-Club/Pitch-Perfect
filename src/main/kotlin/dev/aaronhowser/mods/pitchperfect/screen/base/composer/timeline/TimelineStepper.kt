package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.util.ClientUtil
import dev.aaronhowser.mods.pitchperfect.util.ModClientScheduler
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

    private var playing = false
    fun startPlaying() {
        if (playing) return
        playing = true

        playBeat()
    }

    fun stopPlaying() {
        if (!playing) return
        playing = false
    }

    private fun playBeat() {
        if (!playing) return

        val blockPos = timeline.composerScreen.composerBlockEntity.blockPos

        for (cell in cellsAtBeat) {
            val note = cell.note

            for (soundHolder in cell.sounds) {
                ClientUtil.playNote(
                    soundHolder.value(),
                    note.getGoodPitch(),
                    blockPos.x + 0.5,
                    blockPos.y + 1.5,
                    blockPos.z + 0.5,
                    false
                )
            }

        }

        ModClientScheduler.scheduleTaskInTicks(Timeline.TICKS_PER_BEAT) {
            if (currentDelay >= timeline.lastBeatDelay) {
                stopPlaying()
            } else {
                currentDelay += Timeline.TICKS_PER_BEAT
                playBeat()
            }
        }
    }

}