package com.drathonix.experiencedworlds.common.config;

import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.experiencedworlds.common.fairness.FairnessTravelAlgorithm;
import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.annotations.Save;
import com.vicious.persist.annotations.Typing;

import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.shortcuts.PersistShortcuts;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EWCFG  {
    @PersistentPath
    public static final String path = "config/experiencedworlds.txt";


    public static String safeRL(ResourceLocation rl){
        return rl.toString().replaceFirst(":","/");
    }

    public static ResourceLocation safeRL(String rl){
        return ResourceLocation.parse(rl.replaceFirst("/",":"));
    }

    public static void init() {
        Stringify.register(ResourceLocation.class, EWCFG::safeRL,EWCFG::safeRL);
        //? <1.21.2 {
        /*gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","mined")));
        gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","killed")));
        *///?}
        //? >1.21.2 {
        gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","mined")).get().value());
        gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","killed")).get().value());
        //?}
        announcements.add(AnnouncementType.ADVANCEMENTS);
        announcements.add(AnnouncementType.BORDER_GROWTH);
        PersistShortcuts.init(EWCFG.class);
    }

    public static void reload(){
        PersistShortcuts.init(EWCFG.class);
        ExperiencedBorderManager.growBorder();
    }

    @Save
    @Typing(AnnouncementType.class)
    public static Set<AnnouncementType> announcements = new HashSet<>();

    @Save(description = "This config is balanced for vanilla minecraft. Modifying one value may put everything out of balance.")
    public static Gameplay gameplay = new Gameplay();

    @Save(description = "The time in milliseconds where all experienced worlds data is autosaved. Recommend at least 15 seconds. This is included to prevent data loss on server crash.")
    public static long autoSaveIntervalMS = 1000*60;

    @Save(description = "Configure the fairness checker system.")
    public static FairnessChecker fairnessChecker = new FairnessChecker();


    public static boolean sendAdvancementAnnouncements(){
        return announcements.contains(AnnouncementType.ADVANCEMENTS);
    }

    public static boolean sendBorderGrowthAnnouncements(){
        return announcements.contains(AnnouncementType.BORDER_GROWTH);
    }

    public static class FairnessChecker {
        @Save
        public boolean enabled = true;
        @Save(description = "The maximum amount of time spent finding a viable spawn position. By default the vanilla world border is set to 0,0. Very often this can be in the middle of the ocean, the mod will try to find a valid spawn as close to 0,0 as it can.")
        public long searchMaximumTime = 60L;
        @Save(description = "Distance in blocks to travel between spawnpoint checks. Higher lengths can result in spawns further from 0,0. Lower lengths increase precision but can extend search times.")
        public int length = 5;
        @Save(description = "When true, starts searching for a fair spawnpoint at 0,0. When false starts by searching from the world spawn (not necessarily 0,0).")
        public boolean startAtZeroZero = false;
        @Save(description = "The travel algorithm to use. LINE: searches in one direction. RANDOM: selects a random position in a square area, the area expands by length only when all positions have been checked.")
        public FairnessTravelAlgorithm travelAlgorithm = FairnessTravelAlgorithm.RANDOM;
        @Save(description = "Panic mode multiplier increases travel speed if the biome is blacklisted.")
        public int panicModeMultiplier = 10;
        @Save(description = "Surface Biomes considered unsafe always.")
        @Typing(ResourceLocation.class)
        public Set<ResourceLocation> biomeBlacklist = new HashSet<>();
        {
            biomeBlacklist.add(Biomes.OCEAN.location());
            for (Field field : Biomes.class.getDeclaredFields()) {
                try {
                    if(field.get(Biomes.class) instanceof ResourceKey<?> key){
                        if(key.location().getPath().contains("ocean")){
                            biomeBlacklist.add(key.location());
                        }
                    }
                } catch (IllegalAccessException e) {

                }
            }
            biomeBlacklist.add(Biomes.PLAINS.location());
            biomeBlacklist.add(Biomes.SUNFLOWER_PLAINS.location());
            biomeBlacklist.add(Biomes.SNOWY_PLAINS.location());
            biomeBlacklist.add(Biomes.SNOWY_SLOPES.location());
            biomeBlacklist.add(Biomes.ICE_SPIKES.location());
            biomeBlacklist.add(Biomes.SNOWY_BEACH.location());
            biomeBlacklist.add(Biomes.MEADOW.location());
            biomeBlacklist.add(Biomes.FROZEN_RIVER.location());
            biomeBlacklist.add(Biomes.DESERT.location());
            biomeBlacklist.add(Biomes.JAGGED_PEAKS.location());
            biomeBlacklist.add(Biomes.STONY_PEAKS.location());
            biomeBlacklist.add(Biomes.STONY_SHORE.location());
            biomeBlacklist.add(Biomes.BADLANDS.location());
            biomeBlacklist.add(Biomes.ERODED_BADLANDS.location());
            biomeBlacklist.add(Biomes.RIVER.location());
        }
        @Save(description = "Conditions for a valid spawn")
        public FairnessConditions conditions = new FairnessConditions();

        @Save.Setter("panicModeMultiplier")
        public void setPanicModeMultiplier(int val){
            this.panicModeMultiplier=Math.clamp(val, 1, 200);
        }
    }

    public static class FairnessConditions{
        @Save(description = "Radius in blocks to search for valid spawn blocks in. Increasing this number will slow down the searcher.")
        public int radius = 5;
        @Save(description = "Number of leaf blocks required in the radius for a spawn to be valid.")
        public int leavesRequired = 3;
        @Save(description = "Number of different block types required in the radius for a spawn to be valid.")
        public int blocksRequired = 5;

        @Save.Setter("radius")
        public void setRadius(int radius){
            this.radius=Math.clamp(radius, 0, 20);
        }
    }

    public static class Gameplay  {
        @Save(description = "The multiplier base for advancements.")
        public double advancementMultiplierBase = 1.01D;
        @Save(description = "The maximum advancement multiplier that can be reached")
        public double advancementMultiplierMax = 50.0D;
        @Save(description = "If true the multiplier gained per achievement will be equal to advancementMultiplierBase^numAdvancements, if false: advancementMultiplierBase*numAdvancements")
        public boolean multipliersExponentialGain = true;
        @Save(description = "Rewards a border growth when a stat reaches 1 for the first time. Worlds are permanently influenced by this option, changing it will not grant or ungrant previous stat awards.")
        public boolean awardOne = true;

        @Save(description = "The amount the world grows each time a stat point reaches the required amount.")
        public double sizeGained = 1.0D;

        @Save(description = "The starting world border size.")
        public double startingSize = 1.0D;

        @Save(description = "The amount of time in milliseconds the border takes to grow 0.5 blocks on every edge (aka increase width by 1 block)")
        public long borderGrowthSpeed = 1000L;

        @Save(description = "The max world border size")
        public int maximumBorderSize = Integer.MAX_VALUE;

        @Save(description = "When one of these stats reaches a power of ten (including 1) the border will grow. These are the only supported options: [minecraft:{mined, crafted, used, broken, picked_up, dropped, killed, killed_by}]. There might be other stats that work but I wouldn't recommend trying them as it is untested.")
        @Typing(StatType.class)
        public Set<StatType<?>> activeStats = new HashSet<>();

        @Save(description = "When enabled, the border will grow when a stat reaches a power of 10, when disabled the border will grow everytime a stat grows by 1. If you change this setting, its effects cannot be reversed on worlds where you play with it.")
        public boolean logarithmicStatRequirement = true;

        @Save(description = "The base the logarithmic function should use. When set to 2, the border will grow when stats reach a power of 2, so: 1,2,4,8,16,..., 10 is the default. If you change this setting its effect cannot be reversed on worlds where you play with it.")
        public int logBase = 10;
    }
}
