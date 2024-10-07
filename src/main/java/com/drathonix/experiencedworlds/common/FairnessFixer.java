package com.drathonix.experiencedworlds.common;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class FairnessFixer {
    private static final int range = 5;
    private static BlockPos vec = null;
    private static boolean isOcean = false;
    public static boolean checkFair(int x, int z, Level l){
        AtomicInteger airCount = new AtomicInteger();
        BlockPos topBlock = scanDown(x, z, l, (lev,p,state) -> {
            if (state.getBlock() == Blocks.AIR) {
                airCount.getAndAdd(1);
            } else {
                airCount.set(0);
            }
            return airCount.get() == 2;
        });
        Optional<ResourceKey<Biome>> key = l.getBiome(topBlock).unwrapKey();
        if(key.isPresent()){
            ResourceKey<Biome> k2 = key.get();
            if(k2.location().getPath().contains("ocean")){
                isOcean=true;
                return false;
            }
            else{
                isOcean=false;
            }
        }
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
                    //Spawned on top of tree above stone type block.
                    if(topOption.getZ() == z && topOption.getX() == x && top.requiresCorrectToolForDrops()){
                        return false;
                    }
                    else if(!top.requiresCorrectToolForDrops()) {
                        blocksFound.add(top.getBlock());
                    }
                }
            }
            if(leafPos.size() < 5){
                for (BlockPos p : leafPos) {
                    vec=p;
                    break;
                }
                return false;
            }
            return blocksFound.size() >= 5;
        }
        return false;
    }
    public static BlockPos getFairPos(int x, int z, Level l) throws UnfairnessException {
        int vecX = l.getRandom().nextIntBetweenInclusive(-1,1);
        int vecZ = l.getRandom().nextIntBetweenInclusive(-1,1);
        //Avoid going nowhere.
        if(vecX == vecZ && vecX == 0){
            vecX = -1;
            vecZ = 1;
        }
        vecX*=range;
        vecZ*=range;
        long time = System.currentTimeMillis()+(((long)(int) EWCFG.fairnessCheckMaximumTime)*1000L);
        while(System.currentTimeMillis() < time) {
            if(checkFair(x,z,l)) return scanDown(x,z,l,(lev, p, bs) -> bs.isSolid());
            if(vec != null){
                vecX = vec.getX()/Math.abs(vec.getX());
                vecZ = vec.getZ()/Math.abs(vec.getZ());
            }
            if(!isOcean) {
                x += vecX;
                z += vecZ;
            }
            else{
                x += vecX*20;
                z += vecZ*20;
            }
        }
        throw new UnfairnessException();
    }
    private static final Set<Block> unsafeBlocks = Set.of(Blocks.ICE,Blocks.BLUE_ICE,Blocks.STONE,Blocks.CALCITE,Blocks.PACKED_ICE);
    public static boolean isSafeSpawnBlock(BlockState state){
        if(state.liquid()){
            return false;
        }
        else if(state.requiresCorrectToolForDrops()){
            isOcean=true;
            return false;
        }
        return !unsafeBlocks.contains(state.getBlock());
    }
    public static BlockPos scanDown(int x, int z, Level l, TriPredicate<BlockGetter, BlockPos, BlockState> validator){
        return scanDown(x,l.getHeight(),z,l,validator);
    }
    public static BlockPos scanDown(int x, int y, int z, Level l, TriPredicate<BlockGetter, BlockPos, BlockState> validator){
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
