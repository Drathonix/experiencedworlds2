package com.drathonix.experiencedworlds.common.config;

import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.annotations.Save;
import com.vicious.persist.annotations.Typing;

import com.vicious.persist.shortcuts.PersistShortcuts;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;

import java.util.HashSet;
import java.util.Set;

public class EWCFG  {
    @PersistentPath
    public static final String path = "config/experiencedworlds.txt";

    public static void init() {
        gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","mined")));
        gameplay.activeStats.add(BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryBuild("minecraft","killed")));
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

    @Save(description = "The maximum amount of time spent finding a viable spawn position. By default the vanilla world border is set to 0,0. Very often this can be in the middle of the ocean, the mod will try to find a valid spawn as close to 0,0 as it can.")
    public static long fairnessCheckMaximumTime = 60L;

    public static boolean sendAdvancementAnnouncements(){
        return announcements.contains(AnnouncementType.ADVANCEMENTS);
    }

    public static boolean sendBorderGrowthAnnouncements(){
        return announcements.contains(AnnouncementType.BORDER_GROWTH);
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
