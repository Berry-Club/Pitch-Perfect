package dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline

class TimelineStepper(
    private val timeline: Timeline
) {

    var currentBeat = 0
        set(value) {
            field = value.coerceIn(0, timeline.lastBeatDelay)
        }

}