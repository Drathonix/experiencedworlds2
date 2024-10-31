package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.common.bridge.IMixinDimensionDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(DimensionDataStorage.class)
public abstract class MixinDimensionDataStorage implements IMixinDimensionDataStorage {
    //? <1.21.2
    /*@Shadow @Final private Map<String, SavedData> cache;*/
    //? >1.21.1
    @Shadow @Final private Map<String, Optional<SavedData>> cache;

    //? <1.21.2
    /*@Shadow protected abstract File getDataFile(String string);*/
    //? >1.21.1
    @Shadow protected abstract Path getDataFile(String par1);

    @Shadow @Final private HolderLookup.Provider registries;

    @Shadow
    private static @NotNull CompletableFuture<Void> tryWriteAsync(Path par1, CompoundTag par2) {
        return null;
    }


    @Shadow @Final private Path dataFolder;

    @Override
    public void ss$forceSave(String key) {
        try {
            //? <1.21.2 {
            /*SavedData data = this.cache.get(key);
            if (data != null) {
            *///?}
            //? >1.21.1 {
            Optional<SavedData> dat = this.cache.get(key);
            if(dat.isPresent()) {
                SavedData data = dat.get();
            //?}
                //? <1.21.2
                /*data.save(this.getDataFile(key), this.registries);*/
                //? >1.21.1
                tryWriteAsync(this.getDataFile(key),data.save(this.registries)).join();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public File ss$getCustomDataFile(String name) {
        return new File(this.dataFolder.toFile(), name);
    }
}
