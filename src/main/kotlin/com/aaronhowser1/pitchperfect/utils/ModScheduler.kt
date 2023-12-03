package com.aaronhowser1.pitchperfect.utils

import com.aaronhowser1.pitchperfect.PitchPerfect
import com.google.common.collect.HashMultimap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object ModScheduler {

    private var scheduler: ScheduledExecutorService? = null
    private val scheduledSyncTasks = HashMultimap.create<Int, Runnable>()

    private var currentTick = 0

    fun tick() {
        currentTick++
        handleSyncScheduledTasks(currentTick)
    }

    fun scheduleSynchronisedTask(run: Runnable, ticks: Int) {
        scheduledSyncTasks.put(currentTick + ticks, run)
    }

    fun scheduleAsyncTask(run: Runnable, time: Int, unit: TimeUnit) {
        if (scheduler == null) serverStartupTasks()
        scheduler!!.schedule(run, time.toLong(), unit)
    }

    fun serverStartupTasks() {
        if (scheduler != null) scheduler!!.shutdownNow()
        scheduler = Executors.newScheduledThreadPool(1)
        handleSyncScheduledTasks(null)
    }

    fun serverShutdownTasks() {
        handleSyncScheduledTasks(null)
        scheduler!!.shutdownNow()
        scheduler = null
    }

    fun handleSyncScheduledTasks(tick: Int?) {
        if (scheduledSyncTasks.containsKey(tick)) {
            val tasks =
                if (tick == null) scheduledSyncTasks.values().iterator() else scheduledSyncTasks[tick].iterator()
            while (tasks.hasNext()) {
                try {
                    tasks.next().run()
                } catch (ex: Exception) {
                    PitchPerfect.LOGGER.error("Unable to run unhandled scheduled task, skipping.", ex)
                }
                tasks.remove()
            }
        }
    }

}