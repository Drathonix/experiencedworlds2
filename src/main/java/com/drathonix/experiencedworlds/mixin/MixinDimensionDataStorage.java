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

    //? >1.21.1 && <1.21.4 {
    /*@Shadow
    private static @NotNull CompletableFuture<Void> tryWriteAsync(Path par1, CompoundTag par2) {
        return null;
    }
    *///?}
    //? >1.21.3 {
    @Shadow
    private static void tryWrite(Path par1, CompoundTag par2) {
    }
    //?}

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
                //? >1.21.1 && <1.21.4
                /*tryWriteAsync(this.getDataFile(key),data.save(this.registries)).join();*/
                //? >1.21.3
                tryWrite(this.getDataFile(key),data.save(this.registries));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //? <1.21.2 {
    /*@Shadow @Final private File dataFolder;

    @Override
    public File ss$getCustomDataFile(String name) {
        return new File(this.dataFolder, name);
    }

    @Override
    public File ss$getWorldDir() {
        return this.dataFolder.getParentFile();
    }
    *///?}

    //? >1.21.1 {
    @Shadow @Final private Path dataFolder;

    @Override
    public File ss$getCustomDataFile(String name) {
        return new File(this.dataFolder.toFile(), name);
    }

    @Override
    public File ss$getWorldDir() {
        return this.dataFolder.toFile().getParentFile();
    }
    //?}
}
