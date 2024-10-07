package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.common.bridge.IMixinStatsCounter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatsCounter.class)
public class MixinStatsCounter implements IMixinStatsCounter {
    @Shadow @Final protected Object2IntMap<Stat<?>> stats;

    @Override
    public Object2IntMap<Stat<?>> ss$getStats() {
        return stats;
    }
}
