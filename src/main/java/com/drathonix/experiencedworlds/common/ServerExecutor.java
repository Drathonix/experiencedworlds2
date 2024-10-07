package com.drathonix.experiencedworlds.common;

import java.util.concurrent.*;

public class ServerExecutor {
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    public static ScheduledFuture<?> execute(Runnable run){
        return executor.schedule(run,0, TimeUnit.NANOSECONDS);
    }
}
