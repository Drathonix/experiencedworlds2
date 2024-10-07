package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.common.bridge.IMixinServerStatsCounter;
import net.minecraft.stats.ServerStatsCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerStatsCounter.class)
public abstract class MixinServerStatsCounter implements IMixinServerStatsCounter {
    @Shadow protected abstract String toJson();

    @Override
    public String ss$toJSON() {
        return toJson();
    }
}
