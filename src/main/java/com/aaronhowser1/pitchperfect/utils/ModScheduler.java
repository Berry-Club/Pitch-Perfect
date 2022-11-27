package com.aaronhowser1.pitchperfect.utils;

import com.aaronhowser1.pitchperfect.event.ModEvents;
import com.google.common.collect.HashMultimap;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModScheduler {
    private static ScheduledExecutorService scheduler = null;
    private static final HashMultimap<Integer, Runnable> scheduledSynchTasks = HashMultimap.<Integer, Runnable>create();

    public static void scheduleSynchronisedTask(Runnable run, int ticks) {
        scheduledSynchTasks.put(ModEvents.tick + ticks, run);
    }

    public static void scheduleAsyncTask(Runnable run, int time, TimeUnit unit) {
        if (scheduler == null)
            serverStartupTasks();

        scheduler.schedule(run, time, unit);
    }

    public static void serverStartupTasks() {
        if (scheduler != null)
            scheduler.shutdownNow();

        scheduler = Executors.newScheduledThreadPool(1);

        handleSyncScheduledTasks(null);
    }

    public static void serverShutdownTasks() {
        handleSyncScheduledTasks(null);

        scheduler.shutdownNow();
        scheduler = null;
    }

    public static void handleSyncScheduledTasks(@Nullable Integer tick) {
        if (scheduledSynchTasks.containsKey(tick)) {
            Iterator<Runnable> tasks = tick == null ? scheduledSynchTasks.values().iterator() : scheduledSynchTasks.get(tick).iterator();

            while (tasks.hasNext()) {
                try {
                    tasks.next().run();
                }
                catch (Exception ex) {
//                    Logging.logMessage(Level.ERROR, "Unable to run unhandled scheduled task, skipping.", ex);
                }

                tasks.remove();
            }
        }
    }
}
