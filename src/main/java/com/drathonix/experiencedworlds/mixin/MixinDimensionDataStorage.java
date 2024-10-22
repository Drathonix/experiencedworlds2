package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.common.bridge.IMixinDimensionDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.util.Map;

@Mixin(DimensionDataStorage.class)
public abstract class MixinDimensionDataStorage implements IMixinDimensionDataStorage {
    @Shadow @Final private Map<String, SavedData> cache;

    @Shadow protected abstract File getDataFile(String string);

    @Shadow @Final private HolderLookup.Provider registries;

    @Override
    public void ss$forceSave(String key) {
        try {
            SavedData data = this.cache.get(key);
            if (data != null) {
                data.save(this.getDataFile(key), this.registries);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
