![](https://i.imgur.com/fNbXn6I.png)

**<span style="color:red">Warning: This mod is intended for new worlds. Experienced Worlds was created with the intent of providing a new experience rather than augmenting an old one. If you choose to use this mod on an old singleplayer or multiplayer world be warned that the border may not be safe!</span>**

Ever played that one adventure map where you spawn in a world with a 1x1 world border and you can expand it by earning achievements? Experienced worlds does that but with any seed, albeit only possible seeds.

The border grows when server wide statistics increase. By default, Mining blocks and killing mobs will grow the border. Everytime a stat reaches a power of ten: 1,10,100,1000,... the border grows. Use /serverstats to see the combined stats of all players.

Of course, most of the mod's mechanics are configurable.

[![](https://i.imgur.com/OPwV6sN.png)](https://discord.gg/rsYYBgwnRJ "Vicious")

**Join the Vicious Development discord for updates and for more modded content coming in the future!**

![](https://i.imgur.com/UHYSHAV.png)

The amount of blocks the border grows each time is equal to sizeGained.  
Each time the border size or the advancement multiplier changes, the border expands.

The advancement multiplier increases every time an advancement is earned for the first time, meaning each advancement only contributes once to the growth of the border on servers.
  
The multiplier grows by advancementMultiplierBase^numadvancementsgained each time an advancement is earned.

Modded blocks and advancements are supported, so pack creators should configure the mod as they see fit. Some advancements may be hidden and won't send messages in the chat. These still contribute to the multiplier.

Note: most worlds are not possible to grow beyond a certain distance due to the amount of blocks and the lack of wood nearby. Fortunately for you, this mod comes with a fairness system which will find spawns that are deemed possible. You may still get some cursed spawn locations, but this system will at the bare minimum guarantee the presence of a tree nearby which is essential.

**Commands:**

All Users: /experiencedworlds border <size,multiplier,expansions> shows information about the border state.  
Admins on servers, or in singleplayer/lan: /experiencedworlds config reload - Reloads the config  
Admins only (cheats on in singleplayer): /experiencedworlds world <bonusbordermultiplier,bonusbordersize> <value> allows adding a multiplier and size to the current dimension you are in.

**Configuration (file located in config/experiencedworlds):**

Most of this mod's features are configurable including: the speed the border expands, the amount the border expands, the starting size of the border, the stats that contribute to border growth, etc. If you want something that is not configurable to be added, feel free to ask!

**Current state of Stability**
As of 10/7, version 1.1.6 and below of Experienced Worlds have been effectively expired and completely replaced by V 2.0.0+. Updating worlds running 1.1.6 will result in data loss. This decision was made to improve the codebase and provide a long-lasting stable version of the mod.

**Dependencies**
V-2.0.0 has no dependencies, however V-1.1.6 and below require ViciousCore and Server Statistics.
