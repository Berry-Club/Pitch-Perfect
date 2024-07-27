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

    val topPos by lazy { composerScreen.topPos + 60 }
    val leftPos by lazy { composerScreen.leftPos + 85 }

    var horizontalScrollIndex: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
            timelineStepper.setCellsAtBeat()
        }

    var lastBeatDelay: Int = 0
        private set

    fun init() {
        addTimelineCells()
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

//    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
//        renderTimelineCells(pGuiGraphics, pMouseX, pMouseY)
//    }
//
//    private fun renderTimelineCells(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
//        for (cell in timelineCells) {
//            cell.render(pGuiGraphics, pMouseX, pMouseY)
//        }
//    }


    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        if (composerScreen.selectedInstrument == null) return

        for (cell in timelineCells) {
//            cell.click(pMouseX.toInt(), pMouseY.toInt(), pButton)
        }

        setLastBeatDelay()
    }

    fun setLastBeatDelay() {
        val songWip = composerScreen.songWip ?: return
        val lastBeat = songWip.song.beats.flatMap { it.value }.maxByOrNull { it.at } ?: return
        lastBeatDelay = lastBeat.at
    }

}