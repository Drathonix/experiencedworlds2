package com.drathonix.serverstatistics.common.storage;


import com.drathonix.event.GlobalEvents;
import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.experiencedworlds.common.data.WorldSpecificExperiencedBorder;
import com.drathonix.serverstatistics.common.bridge.IMixinDimensionDataStorage;
import com.drathonix.serverstatistics.common.bridge.IMixinServerAdvancementManager;
import com.drathonix.serverstatistics.common.bridge.IMixinServerStatsCounter;
import com.drathonix.serverstatistics.common.event.AdvancedFirstTimeEvent;
import com.drathonix.serverstatistics.common.event.AdvancementCompletelyRevokedEvent;
import com.drathonix.serverstatistics.common.event.ServerStatsResetEvent;
import com.drathonix.serverstatistics.common.event.StatChangedEvent;
import com.mojang.datafixers.DataFixer;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.AdvancementCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.AdvancementsFix;
import net.minecraft.world.level.saveddata.SavedData;

import java.awt.*;
import java.util.*;
import java.util.function.Supplier;

public class StatData extends SavedData{
    private static final SavedData.Factory<StatData> FACTORY = new SavedData.Factory<>(StatData::new,(compound, holder)->new StatData(compound), DataFixTypes.LEVEL);

    public final Map<ResourceLocation,Set<UUID>> completedAdvancements = new HashMap<>();
    public final Set<UUID> participants = new HashSet<>();
    public ServerStatsCounter counter = new ServerStatsCounter(ExperiencedWorlds.server,new FakeFile(""));

    public StatData() {}

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag inner = new CompoundTag();
        completedAdvancements.forEach((k,v)->{
            ListTag out = new ListTag();
            for (UUID uuid : v) {
                out.add(NbtUtils.createUUID(uuid));
            }
            inner.put(k.toString(),out);
        });
        compoundTag.put("advancements", inner);
        ListTag uuids = new ListTag();
        for (UUID uuid : participants) {
            uuids.add(NbtUtils.createUUID(uuid));
        }
        compoundTag.put("participants", uuids);
        if(counter instanceof IMixinServerStatsCounter mixin) {
            compoundTag.putString("counter", mixin.ss$toJSON());
        }
        return compoundTag;
    }

    public static StatData get(MinecraftServer minecraftServer) {
        return minecraftServer.overworld().getDataStorage().computeIfAbsent(FACTORY,"server_statistics_manager");
    }

    public StatData(CompoundTag tag) {
        CompoundTag advanced = tag.getCompound("advancements");
        for (String key : advanced.getAllKeys()) {
            ListTag achievers = tag.getList(key,Tag.TAG_INT_ARRAY);
            Set<UUID> uuids = completedAdvancements.computeIfAbsent(ResourceLocation.tryParse(key),k-> new HashSet<>());
            for (Tag achiever : achievers) {
                uuids.add(NbtUtils.loadUUID(achiever));
            }
        }
        ListTag partic = tag.getList("participants",Tag.TAG_INT_ARRAY);
        for (Tag tag1 : partic) {
            participants.add(NbtUtils.loadUUID(tag1));
        }
        counter.parseLocal(DataFixers.getDataFixer(),tag.getString("counter"));
        if(partic.isEmpty()) {
            GlobalEvents.post(new ServerStatsResetEvent());
        }
        setDirty();

    }

    public void advanceDone(Advancement advancement, ServerPlayer sp){
        if(advancement.display().isEmpty()){
            return;
        }
        ResourceLocation id = IMixinServerAdvancementManager.getId(sp.getServer(),advancement);
        if(id==null){
            return;
        }
        Set<UUID> holders = completedAdvancements.computeIfAbsent(id,k->new HashSet<>());
        holders.add(sp.getUUID());
        if(holders.size() == 1){
            GlobalEvents.post(new AdvancedFirstTimeEvent(sp,advancement));
            setDirty();
        }
    }

    public void advanceRevoked(Advancement advancement, ServerPlayer sp){
        if(!advancement.display().isPresent()){
            return;
        }
        ResourceLocation id = IMixinServerAdvancementManager.getId(sp.getServer(),advancement);
        if(id==null){
            return;
        }
        Set<UUID> holders = completedAdvancements.getOrDefault(id, new HashSet<>());
        if(holders.isEmpty()){
            GlobalEvents.post(new AdvancementCompletelyRevokedEvent(sp,advancement));
            setDirty();
        }
    }

    public void awardStat(Stat<?> stat, int value, ServerPlayer player) {
        //It might be possible for time related statistics to exceed the maximum integer limit. For this reason I find it important to put a safe-guard in.
        //This would really only occur of many players were constantly online, adding their maximum playtime together and producing enormous numbers.
        int statVal = counter.getValue(stat);
        if(statVal < Integer.MAX_VALUE) {
            StatChangedEvent sae = new StatChangedEvent(value,stat,player);
            GlobalEvents.post(sae);
            if(!sae.isCanceled()) {
                counter.setValue(null, stat, statVal+value);
            }
            setDirty();
        }
    }

    public void forceSave(){
        if(ExperiencedWorlds.server.overworld().getChunkSource().getDataStorage() instanceof IMixinDimensionDataStorage mixin){
            mixin.ss$forceSave("server_statistics_manager");
        }
    }

    public void resetStat(Stat<?> stat, ServerPlayer player) {
        awardStat(stat, -counter.getValue(stat),player);
    }
}
