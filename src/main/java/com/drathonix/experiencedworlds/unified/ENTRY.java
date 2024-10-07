package com.drathonix.experiencedworlds.unified;
import com.drathonix.experiencedworlds.ExperiencedWorlds;

//? if >1.18.2
import net.fabricmc.api.ModInitializer;


//? if fabric {
public class ENTRY implements ModInitializer {
        @Override
        public void onInitialize() {
                ExperiencedWorlds.init();
        }
}
//?}

//? elif forge {
/*import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraftforge.fml.common.Mod;

@Mod(LoadMyChunks.MOD_ID)
public class ENTRY {
    public ENTRY() {
        LMCForge.init();
    }
}
*///?}

//? elif neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
@Mod(ExperiencedWorlds.MOD_ID)
public class ENTRY {
    public ENTRY(IEventBus meb) {
        ExperiencedWorlds.init();
    }
}
*///?}

