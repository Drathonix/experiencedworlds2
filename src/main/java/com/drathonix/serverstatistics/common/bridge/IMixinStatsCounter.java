package com.drathonix.serverstatistics.common.bridge;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.stats.Stat;

public interface IMixinStatsCounter {
    Object2IntMap<Stat<?>> ss$getStats();

}
