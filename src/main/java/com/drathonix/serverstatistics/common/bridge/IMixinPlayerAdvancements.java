package com.drathonix.serverstatistics.common.bridge;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;

import java.util.Map;

public interface IMixinPlayerAdvancements {
    Map<AdvancementHolder, AdvancementProgress> getProgress();
}
