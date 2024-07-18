package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ScreenInstrument

class TimelineData {

    private data class InstrumentCounter(
        private val amountInstruments: MutableMap<ScreenInstrument, Int>
    ) {
        fun increment(instrument: ScreenInstrument) {
            val current = amountInstruments.getOrDefault(instrument, 0)
            amountInstruments[instrument] = current + 1
        }

        fun decrement(instrument: ScreenInstrument) {
            val current = amountInstruments[instrument] ?: return
            amountInstruments[instrument] = current - 1
        }

        fun get(instrument: ScreenInstrument): Int {
            return amountInstruments.getOrDefault(instrument, 0)
        }

        fun getAll(): Map<ScreenInstrument, Int> {
            return amountInstruments.toMap()
        }
    }

    private class TimelineCellDat(

    )

    private val

}