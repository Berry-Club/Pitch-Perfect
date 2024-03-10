package dev.aaronhowser.mods.pitchperfect.event

import com.google.common.collect.HashMultimap
import dev.aaronhowser.mods.pitchperfect.PitchPerfect

object ModScheduler {

    private val upcomingTasks = HashMultimap.create<Int, Runnable>()

    var tick = 0
        set(value) {
            field = value
            handleScheduledTasks(value)
        }

    fun scheduleTaskInTicks(ticks: Int, run: Runnable) {
        upcomingTasks.put(tick + ticks, run)
    }

    private fun handleScheduledTasks(tick: Int?) {
        if (!upcomingTasks.containsKey(tick)) return

        val tasks = upcomingTasks[tick].iterator()

        while (tasks.hasNext()) {
            try {
                tasks.next().run()
            } catch (e: Exception) {
                PitchPerfect.LOGGER.error(e.toString())
            }

            tasks.remove()
        }
    }

}