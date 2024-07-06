package dev.aaronhowser.mods.pitchperfect.util

import com.google.common.collect.HashMultimap
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(
    modid = PitchPerfect.ID
)
object ModClientScheduler {

    @SubscribeEvent
    fun onServerTick(event: ClientTickEvent.Post) {
        currentTick++
    }

    var currentTick = 0
        set(value) {
            field = value
            handleScheduledTasks(value)
        }

    private val upcomingTasks: HashMultimap<Int, Runnable> = HashMultimap.create()

    fun scheduleTaskInTicks(ticksInFuture: Int, runnable: Runnable) {
        if (ticksInFuture > 0) {
            upcomingTasks.put(currentTick + ticksInFuture, runnable)
        } else {
            runnable.run()
        }
    }

    private fun handleScheduledTasks(tick: Int) {

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