package com.aaronhowser1.pitchperfect.utils

import com.google.common.collect.HashMultimap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object ModScheduler {

    private var scheduler: ScheduledExecutorService? = null
    private val scheduledSyncTasks = HashMultimap.create<Int, Runnable>()

    fun scheduleSynchronisedTask(run: Runnable, ticks: Int) {
        scheduledSyncTasks.put(ModEvents.tick + ticks, run)
    }

    fun scheduleAsyncTask(run: Runnable?, time: Int, unit: TimeUnit?) {
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
//                    Logging.logMessage(Level.ERROR, "Unable to run unhandled scheduled task, skipping.", ex);
                }
                tasks.remove()
            }
        }
    }

}