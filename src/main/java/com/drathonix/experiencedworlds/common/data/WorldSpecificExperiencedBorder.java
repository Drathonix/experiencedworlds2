package com.drathonix.experiencedworlds.common.data;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldSpecificExperiencedBorder extends SavedData {
    private final ServerLevel level;

    public WorldSpecificExperiencedBorder(ServerLevel level) {
        this.level = level;
    }

    public WorldSpecificExperiencedBorder(CompoundTag compoundTag, ServerLevel level) {
        multiplier = compoundTag.getDouble("multiplier");
        startingSize = compoundTag.getDouble("startingSize");
        this.level=level;
    }

    public static WorldSpecificExperiencedBorder get(ServerLevel level){
        SavedData.Factory<WorldSpecificExperiencedBorder> FACTORY = new SavedData.Factory<>(()->new WorldSpecificExperiencedBorder(level),(compound, holder)->new WorldSpecificExperiencedBorder(compound, level), DataFixTypes.LEVEL);
        return level.getDataStorage().computeIfAbsent(FACTORY,"specific_experienced_worlds_manager");
    }

    public double multiplier = 0.0;
    public double startingSize = 0.0;

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putDouble("multiplier", multiplier);
        compoundTag.putDouble("startingSize", startingSize);
        return compoundTag;
    }

    public void shift(ExperiencedBorderManager swb, boolean doFastExpand){
        double newSize = swb.getTransformedBorderSize()*Math.max(1,multiplier)+startingSize;
        WorldBorder border = level.getWorldBorder();
        double size = border.getSize();
        long change = (long) Math.ceil(Math.abs(newSize - size));
        if(size <= newSize) {
            border.lerpSizeBetween(size, newSize, change * (!doFastExpand ? EWCFG.gameplay.borderGrowthSpeed : 1L) + border.getLerpRemainingTime());
        }
        else{
            border.lerpSizeBetween(size, newSize, -change * (!doFastExpand ? EWCFG.gameplay.borderGrowthSpeed : 1L) + border.getLerpRemainingTime());
        }
    }
}
