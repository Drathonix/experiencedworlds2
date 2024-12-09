package com.drathonix.experiencedworlds.unified;
import com.drathonix.experiencedworlds.ExperiencedWorlds;

//? if >1.18.2


//? if fabric {
import net.fabricmc.api.ModInitializer;

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

@Mod(ExperiencedWorlds.MOD_ID)
public class ENTRY {
    public ENTRY() {
        ExperiencedWorlds.init();
    }
}
*///?}

//? elif neoforge {
/*import com.drathonix.experiencedworlds.neoforge.NeoForgeInit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
@Mod(ExperiencedWorlds.MOD_ID)
public class ENTRY {
    public ENTRY(IEventBus meb) {
        ExperiencedWorlds.init();
        NeoForgeInit.init(meb);
    }
}
*///?}

