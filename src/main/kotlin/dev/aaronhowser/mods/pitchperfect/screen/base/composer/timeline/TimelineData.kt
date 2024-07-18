package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ScreenInstrument

class TimelineData(
    private val timeline: Timeline
) {

    data class CellData(
        private val instrumentsCounter: MutableMap<ScreenInstrument, Int>
    ) {
        fun increment(instrument: ScreenInstrument) {
            val current = instrumentsCounter.getOrDefault(instrument, 0)
            instrumentsCounter[instrument] = current + 1
        }

        fun decrement(instrument: ScreenInstrument) {
            val current = instrumentsCounter[instrument] ?: return
            instrumentsCounter[instrument] = current - 1

            if (instrumentsCounter[instrument] == 0) instrumentsCounter.remove(instrument)
        }

        fun get(instrument: ScreenInstrument): Int {
            return instrumentsCounter.getOrDefault(instrument, 0)
        }

        fun getAllCounts(): Map<ScreenInstrument, Int> {
            return instrumentsCounter.toMap()
        }

        fun isEmpty(): Boolean {
            return instrumentsCounter.isEmpty()
        }
    }

    private object Cells {
        private val cells: MutableMap<Pair<Int, Int>, CellData> = mutableMapOf()

        fun increment(delay: Int, pitch: Int, instrument: ScreenInstrument) {
            val cellData = cells.getOrPut(Pair(delay, pitch)) { CellData(mutableMapOf()) }
            cellData.increment(instrument)
        }

        fun decrement(delay: Int, pitch: Int, instrument: ScreenInstrument) {
            val cellData = cells[Pair(delay, pitch)] ?: return

            cellData.decrement(instrument)
            if (cellData.isEmpty()) cells.remove(Pair(delay, pitch))
        }

        fun getCell(delay: Int, pitch: Int): CellData? {
            return cells[Pair(delay, pitch)]
        }
    }

    fun increment(delay: Int, pitch: Int) {
        val instrument = timeline.composerScreen.selectedInstrument ?: return
        Cells.increment(delay, pitch, instrument)
    }

    fun decrement(delay: Int, pitch: Int) {
        val instrument = timeline.composerScreen.selectedInstrument ?: return
        Cells.decrement(delay, pitch, instrument)
    }

    fun getCellData(delay: Int, pitch: Int): CellData? {
        return Cells.getCell(delay, pitch)
    }

}