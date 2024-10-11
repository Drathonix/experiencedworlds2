# Experienced Worlds V-2.0.0

Warning, this version is incompatible with version v-1.1.6. Updating your mod will result in border data loss.

## Changes

Switched from using ViciousCore data storage to directly saving to the world data folder. Integrated Server Statistics directly into the mod.
Now depends on architectury (and fabric-api on fabric).

Config structure has been changed completely and moved to config/experiencedworlds.txt.

Translated messages are now sent with the English translation as a fallback for people who do not have ExperiencedWorlds installed.

Switched to stonecutter for multi-versioning, updating and backporting the mod is now easier.

## Why were these breaking changes made?

The old version used mixins to inject fields into ViciousCore data classes which was extremely scuffed and often encountered issues. In addition, requiring vicious core and server statistics created unnecessary bloat. 2.0 has better code structure and less bloat, it is also just more stable.

### Weird interactions due to vanilla changes

There was something changed about interacting with blocks intercepting the world border. In the past, blocks that intercepted the border were breakable, now its like a 50/50 if this works. I don't know what changed in vanilla to cause this but if its significant enough I may reverse the change, You can still progress EW perfectly fine though.