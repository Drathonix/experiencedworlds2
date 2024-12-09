package com.drathonix.experiencedworlds.common.util;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.math.EWMath;
import com.drathonix.serverstatistics.ServerStatistics;
import com.drathonix.serverstatistics.common.bridge.IMixinStatsCounter;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ExpansionPower {
    public synchronized static double calculateExpansionPower(StatsCounter counter){
        double ep = 0;
        if(counter instanceof IMixinStatsCounter mixin){
            Object2IntMap<Stat<?>> stats = mixin.ss$getStats();
            for (Stat<?> stat : stats.keySet()) {
                if(EWCFG.gameplay.activeStats.contains(stat.getType())){
                    int value = stats.getInt(stat);
                    ep += EWCFG.gameplay.logarithmicStatRequirement ? EWMath.logConfigBase(value) : value;
                    if(value >= 1 && EWCFG.gameplay.awardOne){
                        ep+=1;
                    }
                }
            }
        }
        return ep;
    }
    public synchronized static Pair<Stat<?>,Integer> selectStat(StatsCounter counter, Query query){
        Stat<?> current = null;
        if(counter instanceof IMixinStatsCounter mixin) {
            Object2IntMap<Stat<?>> stats = mixin.ss$getStats();
            for (Stat<?> stat : stats.keySet()) {
                if (EWCFG.gameplay.activeStats.contains(stat.getType())) {
                    current = query.choose(current,current == null ? 0 : stats.getInt(current),stat,stats.getInt(stat));
                }
            }
            return Pair.of(current,current == null ? 0 : stats.getInt(current));
        }
        throw new IllegalStateException("IMixinStatsCounter did not apply.");
    }

    /**
     * @return a list of UUIDs and their respective expansion power. Sorted Greatest to least
     */
    public synchronized static List<Pair<UUID,Double>> getExpansionPowerLeaderBoard(){
        List<Pair<UUID,Double>> out = new ArrayList<>(ServerStatistics.getPlayerStats()
                .stream()
                .map(pair->Pair.of(pair.getFirst(),calculateExpansionPower(pair.getSecond())))
                .sorted(Comparator.comparingDouble((p)->-p.getSecond()))
                .toList());
        out.addFirst(Pair.of(null,calculateExpansionPower(ServerStatistics.getData().counter)));
        return out;

    }

    public synchronized static double calculateTruncatedExpansionPower(StatsCounter counter){
        int ep = 0;
        if(counter instanceof IMixinStatsCounter mixin){
            Object2IntMap<Stat<?>> stats = mixin.ss$getStats();
            for (Stat<?> stat : stats.keySet()) {
                if(EWCFG.gameplay.activeStats.contains(stat.getType())){
                    int value = stats.getInt(stat);
                    ep += EWCFG.gameplay.logarithmicStatRequirement ? (int)EWMath.logConfigBase(value) : value;
                    if(value >= 1 && EWCFG.gameplay.awardOne){
                        ep+=1;
                    }
                }
            }
        }
        return ep;
    }

    @FunctionalInterface
    public interface Query {
        @Nullable Stat<?> choose(@Nullable Stat<?> previous, int previousVal, @Nullable Stat<?> current, int currentVal);
    }
}
