package com.drathonix.experiencedworlds.common.data;

import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.experiencedworlds.common.FairnessFixer;
import com.drathonix.experiencedworlds.common.ServerExecutor;
import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.math.EWMath;
import com.drathonix.experiencedworlds.common.util.EWChatMessage;
import com.drathonix.serverstatistics.ServerStatistics;
import com.drathonix.serverstatistics.common.bridge.IMixinDimensionDataStorage;
import com.drathonix.serverstatistics.common.event.StatChangedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledFuture;

public class ExperiencedBorderManager extends SavedData implements IWorldBorderData{
    public static final SavedData.Factory<ExperiencedBorderManager> FACTORY = new SavedData.Factory<>(ExperiencedBorderManager::new,(compound, holder)->new ExperiencedBorderManager(compound), DataFixTypes.LEVEL);

    public static ExperiencedBorderManager get(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY,"experienced_worlds_manager");
    }

    private static long lastExpand = System.currentTimeMillis();
    public static Difficulty difficulty = null;

    public static void growBorder(){
        ExperiencedBorderManager swb = get(ExperiencedWorlds.server);
        boolean doFastExpand = lastExpand+50 > System.currentTimeMillis();
        for (ServerLevel level : ExperiencedWorlds.server.getAllLevels()) {
            WorldSpecificExperiencedBorder.get(level).shift(swb,doFastExpand);
        }
        lastExpand = System.currentTimeMillis();
    }

    public static void grow(int i){
        ExperiencedBorderManager swb = get(ExperiencedWorlds.server);
        swb.expand(i);
        growBorder();
    }

    public static void onJoin(ServerPlayer sp){

        ExperiencedBorderManager swb = ExperiencedBorderManager.get(ExperiencedWorlds.server);
        ServerLevel sl = sp.serverLevel();
        checkNeedsFixing(sl,swb);
        if (swb.fairness == FairnessLevel.CHECKING) {
            EWChatMessage.from(ChatFormatting.GREEN, ChatFormatting.BOLD, "<experiencedworlds.searchingforsafety>").send(sp);
            sp.setGameMode(GameType.ADVENTURE);
        }
    }

    private static void checkNeedsFixing(ServerLevel sl, ExperiencedBorderManager swb){
        if (swb.fairness == FairnessLevel.UNSET) {
            if (sl.getServer().overworld() == sl) {
                fixBorder(sl,swb);
            }
        }
    }

    static void pauseWorld(ServerLevel sl){
        if(difficulty == null){
            difficulty = sl.getDifficulty();
        }
        else{
            sl.getServer().setDifficulty(difficulty,true);
            difficulty = null;
        }
    }

    public static ScheduledFuture<?> task = null;
    private static void fixBorder(ServerLevel sl, ExperiencedBorderManager swb){
        swb.fairness = FairnessLevel.CHECKING;
        pauseWorld(sl);
        if(task != null){
            task.cancel(true);
            task = null;
        }
        task = ServerExecutor.execute(() -> {
            WorldBorder border = sl.getWorldBorder();
            BlockPos fairCenter = FairnessFixer.scanDown(0, 0, sl, (l, p, bs) -> bs.isCollisionShapeFullBlock(l, p));
            try {
                fairCenter = FairnessFixer.getFairPos(sl.getSharedSpawnPos().getX(), sl.getSharedSpawnPos().getZ(), sl);
                border.setCenter(fairCenter.getX(), fairCenter.getZ());
                swb.fairness = FairnessLevel.FAIR;
            } catch (FairnessFixer.UnfairnessException e) {
                swb.fairness = FairnessLevel.UNFAIR;
            }
            for (ServerPlayer player : sl.getServer().getPlayerList().getPlayers()) {
                //Adventure mode counts.
                if (player.gameMode.isSurvival()) {
                    player.setGameMode(GameType.SURVIVAL);
                }
                if (swb.fairness == FairnessLevel.UNFAIR) {
                    EWChatMessage.from(ChatFormatting.RED, ChatFormatting.BOLD, "<1experiencedworlds.unfairworld>", EWCFG.fairnessCheckMaximumTime).send(player);
                } else {
                    EWChatMessage.from(ChatFormatting.GREEN, ChatFormatting.BOLD, "<experiencedworlds.fairworld>").send(player);
                }
                //? <1.21.2
                /*player.teleportTo(sl, fairCenter.getX(), fairCenter.getY() + 1, fairCenter.getZ(), 0, 0);*/
                //? >1.21.2
                player.teleportTo(fairCenter.getX(), fairCenter.getY() + 1, fairCenter.getZ());
            }
            pauseWorld(sl);
            growBorder();
        });
    }

    private static void relocateToSafeLocation(ServerPlayer player){
        ExperiencedWorlds.server.execute(() -> {
            ServerLevel sl = player.serverLevel();
            WorldBorder border = sl.getWorldBorder();
            BlockPos fairCenter = FairnessFixer.scanDown((int) border.getCenterX(), (int) border.getCenterZ(), sl, (l, p, bs) -> bs.isCollisionShapeFullBlock(l, p));
            //? <1.21.2
            /*player.teleportTo(sl, fairCenter.getX(), fairCenter.getY() + 1, fairCenter.getZ(), 0, 0);*/
            //? >1.21.2
            player.teleportTo(fairCenter.getX(), fairCenter.getY() + 1, fairCenter.getZ());
        });
    }

    public static void increaseBorder(int amount, StatChangedEvent sce){
        ExperiencedBorderManager swb = ExperiencedWorlds.getBorder();
        swb.expand(amount);
        double a2 = Math.round(amount*swb.getSizeMultiplier()*EWCFG.gameplay.sizeGained*100.0)/100.0;
        int current = ServerStatistics.getData().counter.getValue(sce.getStat());
        if(a2 != 1) {
            if(EWCFG.sendBorderGrowthAnnouncements() && !swb.maximumBorderSize()){
                EWChatMessage.from("<3experiencedworlds.grewborderplural>", sce.getPlayer().getDisplayName(), current+1,a2).send(ExperiencedWorlds.server.getPlayerList().getPlayers());
            }
        }
        else{
            if(EWCFG.sendBorderGrowthAnnouncements() && !swb.maximumBorderSize()){
                EWChatMessage.from("<2experiencedworlds.grewborder>", sce.getPlayer().getDisplayName(), current+1).send(ExperiencedWorlds.server.getPlayerList().getPlayers());
            }
        }
        growBorder();
    }

    public int expansions = 0;
    public FairnessLevel fairness = FairnessLevel.UNSET;

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("expansions", expansions);
        compoundTag.putInt("fairness", fairness.ordinal());
        return compoundTag;
    }

    public ExperiencedBorderManager(){}

    public ExperiencedBorderManager(CompoundTag compoundTag) {
        expansions = compoundTag.getInt("expansions");
        fairness = FairnessLevel.values()[compoundTag.getInt("fairness")];
        //Retry check.
        if(fairness == FairnessLevel.CHECKING){
            fairness = FairnessLevel.UNSET;
        }
        setDirty();
    }

    @Override
    public int getExpansions() {
        return expansions;
    }

    @Override
    public void expand(int expansions) {
        this.expansions = expansions+this.expansions;
        this.expansions = Math.max(0,this.expansions);
        setDirty();
    }



    @Override
    public double getTransformedBorderSize() {
        return Math.min(EWCFG.gameplay.startingSize+expansions*EWCFG.gameplay.sizeGained*getSizeMultiplier(),EWCFG.gameplay.maximumBorderSize);
    }

    public boolean maximumMultiplier(){
        return getUnmaxedSizeMultiplier() >= EWCFG.gameplay.advancementMultiplierMax;
    }

    public double getUnmaxedSizeMultiplier(){
        int numAdvancements = ServerStatistics.getData().completedAdvancements.size();
        return numAdvancements <= 0 ? 1 : 1 + EWMath.summate(numAdvancements,d1(),getCurrentMultiplierGain());
    }

    public double getSizeMultiplier() {
        return Math.min(getUnmaxedSizeMultiplier(),EWCFG.gameplay.advancementMultiplierMax);
    }
    private double d1(){
        if(EWCFG.gameplay.multipliersExponentialGain){
            return Math.abs(getMultiplierBase())-1;
        }
        else{
            return Math.abs(getMultiplierBase());
        }
    }
    public double getMultiplierBase(){
        return EWCFG.gameplay.advancementMultiplierBase;
    }

    public double getCurrentMultiplierGain() {
        int numAdvancements = ServerStatistics.getData().completedAdvancements.size();
        double mb = getMultiplierBase();
        return EWCFG.gameplay.multipliersExponentialGain ? EWMath.baseToTheX(mb,numAdvancements,-1) : mb*numAdvancements;
    }
    
    public void reset() {
        expansions = 0;
        growBorder();
        setDirty();
    }

    public void forceSave(){
        if(ExperiencedWorlds.server.overworld().getChunkSource().getDataStorage() instanceof IMixinDimensionDataStorage mixin){
            mixin.ss$forceSave("experienced_worlds_manager");
        }
    }

    public boolean maximumBorderSize() {
        return getTransformedBorderSize() >= EWCFG.gameplay.maximumBorderSize;
    }

}
