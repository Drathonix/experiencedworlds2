package com.drathonix.experiencedworlds.common.fairness;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class FairnessFixer {
    private static boolean panicMode = false;
    public synchronized static boolean checkFair(int x, int z, Level l){
        AtomicInteger airCount = new AtomicInteger();
        Optional<ResourceKey<Biome>> key = l.getBiome(new BlockPos(x,255,z)).unwrapKey();
        if(key.isPresent()){
            if(EWCFG.fairnessChecker.biomeBlacklist.contains(key.get().location())){
                panicMode = true;
                return false;
            }
        }
        BlockPos topBlock = scanDown(x, z, l, (lev,p,state) -> {
            if (state.getBlock() == Blocks.AIR) {
                airCount.getAndAdd(1);
            } else {
                airCount.set(0);
            }
            return airCount.get() == 2;
        });
        int range = EWCFG.fairnessChecker.conditions.radius;
        Set<Block> blocksFound = new HashSet<>();
        Set<BlockPos> leafPos = new HashSet<>();
        if(isSafeSpawnBlock(l.getBlockState(topBlock))){
            for (int x2 = -range; x2 < range; x2++) {
                for (int z2 = -range; z2 < range; z2++) {
                    BlockPos topOption = scanDown(x+x2, z+z2, l, (lev, p, bs) -> bs.isCollisionShapeFullBlock(lev,p));
                    BlockState top = l.getBlockState(topOption);
                    while(!top.requiresCorrectToolForDrops() && !top.isAir()){
                        if(top.getBlock() instanceof LeavesBlock){
                            leafPos.add(topOption);
                        }
                        blocksFound.add(top.getBlock());
                        topOption = topOption.below();
                        top = l.getBlockState(topOption);
                    }
                    while(top.isAir()){
                        topOption = topOption.below();
                        top = l.getBlockState(topOption);
                    }
                    if(!top.requiresCorrectToolForDrops()) {
                        blocksFound.add(top.getBlock());
                    }
                }
            }
            if(leafPos.size() < EWCFG.fairnessChecker.conditions.leavesRequired){
                return false;
            }
            if(blocksFound.size() < EWCFG.fairnessChecker.conditions.blocksRequired){
            }
            return blocksFound.size() >= EWCFG.fairnessChecker.conditions.blocksRequired;
        }
        return false;
    }
    public synchronized static BlockPos getFairPos(int x, int z, ServerLevel l) throws UnfairnessException {
        //? <1.21.2
        /*BlockPos current = new BlockPos(x,l.getMaxBuildHeight(),z);*/
        //? >1.21.1
        BlockPos current = new BlockPos(x,l.getMaxY(),z);
        EWCFG.fairnessChecker.travelAlgorithm.setup(l,current);
        long time = System.currentTimeMillis()+EWCFG.fairnessChecker.searchMaximumTime*1000L;
        while(System.currentTimeMillis() < time) {
            System.out.println("Checking: " + current.getX() + " : " + current.getZ() + ": " + (time-System.currentTimeMillis())/1000);
            if(checkFair(current.getX(),current.getZ(),l)){
                EWCFG.fairnessChecker.travelAlgorithm.cleanup();
                return scanDown(current.getX(),current.getZ(),l,(lev, p, bs) -> bs.isSolid());
            }
            current = EWCFG.fairnessChecker.travelAlgorithm.nextBlockPosition(l,current,panicMode);
            panicMode=false;
        }
        EWCFG.fairnessChecker.travelAlgorithm.cleanup();
        throw new UnfairnessException();
    }
    private static final Set<Block> unsafeBlocks = Set.of(Blocks.ICE,Blocks.BLUE_ICE,Blocks.STONE,Blocks.CALCITE,Blocks.PACKED_ICE);
    public synchronized static boolean isSafeSpawnBlock(BlockState state){
        if(state.liquid()){
            return false;
        }
        else if(state.requiresCorrectToolForDrops()){
            panicMode=true;
            return false;
        }
        return !unsafeBlocks.contains(state.getBlock());
    }
    public synchronized static BlockPos scanDown(int x, int z, Level l, TriPredicate<BlockGetter, BlockPos, BlockState> validator){
        return scanDown(x,l.getHeight(),z,l,validator);
    }
    public synchronized static BlockPos scanDown(int x, int y, int z, Level l, TriPredicate<BlockGetter, BlockPos, BlockState> validator){
        BlockPos pos = new BlockPos(x,y,z);
        BlockState state = l.getBlockState(pos);
        while(!validator.test(l,pos,state)){
            pos = pos.below();
            state = l.getBlockState(pos);
        }
        return pos;
    }

    public static class UnfairnessException extends Exception{}

    @FunctionalInterface
    public interface TriPredicate<A,B,C>{
        boolean test(A a, B b, C c);
    }
}
