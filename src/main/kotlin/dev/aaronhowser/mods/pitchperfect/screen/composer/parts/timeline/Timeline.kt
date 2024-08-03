package dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import net.minecraft.client.gui.Font
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class Timeline(
    val composerScreen: ComposerScreen,
    val font: Font
) {

    companion object {
        const val ROW_COUNT = 25
        const val COLUMN_COUNT = 41

        const val TICKS_PER_BEAT = 2
    }

    //TODO: Scroll bar at the bottom

    val timelineStepper = TimelineStepper(this)

    val topPos by lazy { composerScreen.topPos + 40 }
    val leftPos by lazy { composerScreen.leftPos + 60 }
    val rightPos by lazy { leftPos + COLUMN_COUNT * (TimelineCell.WIDTH + 1) }

    var horizontalScrollIndex: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
            timelineStepper.setCellsAtBeat()
        }

    var lastBeatDelay: Int = 0
        private set

    fun init() {
        addTimelineCells()
        addStepJumpButtons()
        setLastBeatDelay()

        timelineStepper.init()
    }

    var timelineCells: List<TimelineCell> = listOf()
        private set

    private fun addTimelineCells() {
        if (timelineCells.isNotEmpty()) {
            PitchPerfect.LOGGER.error("Tried to add timeline cells when they already exist")
            return
        }

        val tempList = mutableListOf<TimelineCell>()

        for (yIndex in 0 until ROW_COUNT) {
            for (xIndex in 0 until COLUMN_COUNT) {
                tempList += TimelineCell(this, xIndex, yIndex)
            }
        }

        timelineCells = tempList

        composerScreen.addRenderableWidgets(timelineCells)
    }

    var stepJumpButtons: List<StepJumpButton> = listOf()
        private set

    private fun addStepJumpButtons() {
        if (stepJumpButtons.isNotEmpty()) {
            PitchPerfect.LOGGER.error("Tried to add step jump buttons when they already exist")
            return
        }

        val tempList = mutableListOf<StepJumpButton>()

        for (xIndex in 0 until COLUMN_COUNT) {
            tempList += StepJumpButton(this, xIndex)
        }

        stepJumpButtons = tempList
        composerScreen.addRenderableWidgets(stepJumpButtons)
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        setLastBeatDelay()
    }

    fun setLastBeatDelay() {
        val songWip = composerScreen.composerSong ?: return
        val lastBeat = songWip.song.beats.flatMap { it.value }.maxByOrNull { it.at } ?: return
        lastBeatDelay = lastBeat.at
    }

}