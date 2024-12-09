package com.drathonix.experiencedworlds.common.fairness;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum FairnessTravelAlgorithm {
    LINEAR{
        private BlockPos vector;

        @Override
        public void setup(ServerLevel level, BlockPos start) {
            int x = 0, z = 0;
            while(x == 0 && z == 0) {
                x = rand.nextInt(-1, 1);
                z = rand.nextInt(-1, 1);
            }
            vector = new BlockPos(x,0,z);
        }

        @Override
        public BlockPos nextBlockPosition(ServerLevel level, BlockPos previous, boolean panicMode) {
            if(panicMode) {
                return previous.offset(vector.multiply(EWCFG.fairnessChecker.panicModeMultiplier));
            }
            else{
                return previous.offset(vector);
            }
        }
    },
    RANDOM{
        private BlockPos start;
        private final List<BlockPos> toCheck = new ArrayList<>();
        private int range = 0;
        @Override
        public void setup(ServerLevel level, BlockPos start) {
            this.start = start;
            this.toCheck.clear();
            this.range=-1;
            extendRange(false);
        }

        private void extendRange(boolean panicMode){
            int startRange = range+1;
            int endRange = startRange + 1;
            int length = EWCFG.fairnessChecker.length;
            for (int l = -endRange; l < endRange-1; l++) {
                for (int w = startRange; w < endRange; w++) {
                    toCheck.add(start.offset(w*length,0,l*length));
                    toCheck.add(start.offset(w*length,0,l*-length));
                    toCheck.add(start.offset(w*-length,0,l*length));
                    toCheck.add(start.offset(w*-length,0,l*-length));
                }
            }
            if(panicMode){
                range+=EWCFG.fairnessChecker.panicModeMultiplier;
            }
            else {
                range++;
            }
        }

        @Override
        public BlockPos nextBlockPosition(ServerLevel level, BlockPos previous, boolean panicMode) {
            if(toCheck.isEmpty()){
                extendRange(panicMode);
            }
            return toCheck.remove(rand.nextInt(toCheck.size()));
        }

        @Override
        public void cleanup() {
            toCheck.clear();
        }
    };

    private static final Random rand = new Random();

    public void setup(ServerLevel level, BlockPos start){}

    public BlockPos nextBlockPosition(ServerLevel level, BlockPos previous, boolean panicMode){
        return previous;
    }

    public void cleanup(){}
}
