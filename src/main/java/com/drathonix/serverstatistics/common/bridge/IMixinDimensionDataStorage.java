package com.drathonix.serverstatistics.common.bridge;

import net.minecraft.world.level.saveddata.SavedData;

public interface IMixinDimensionDataStorage {
    void ss$forceSave(String key);
}
