# Experienced Worlds V-2.0.5

Added support for 1.21.4! (this is a separate jar unfortunately)

# Reworked the Fairness Checker system.

For some lore, this system went relatively unchanged from the V-1 in the initial V-2 release, so it had some design flaws.

1. Fairness checker now can be disabled. If this is disabled the world border center will be set to the world spawn.
2. Added 2 different block travelling algorithms, selectable in the config.
3. The new default settings work as follows: the initial block is checked, if it is unfair, the checking range is extended and 8 more blocks are checked in a random order until a valid spawn is found.
4. Also, the start position of the spawn checker is set to the world spawn-point.

The fairness checker is now much faster and much more reliable as a result of these changes (most worlds have fair spawns by default).

### Other
Updated to Persist 1.1.6

Added /experiencedworlds recalculate - Recalculates the border expansions (not the multiplier). This may update the border size.
/experiencedworlds forceloadstats now also runs recalculate after wards (there's currently an unknown bug causing this to not work as intended, recalc works fine though).

As a note of why recalculate was added, my server exceeded the storage space which corrupted the experienced worlds data files, re-running forceloadstats should have fixed the problem, but it seems it was broken by the world corruption (and I don't know how). Recalculate is 100% accurate.
If you're wondering about how expansions are calculated, they are only calculated when a stat changes and only for that stat. Recalculate calculates the total expansions for all stats which is much more computationally expensive especially as stats grow.