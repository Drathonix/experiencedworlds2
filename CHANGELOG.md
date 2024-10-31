# Experienced Worlds V-2.0.3

Patches a critical problem with high amounts of statistic data causing the server stats data to become too large to store using an NBT tag. This is encountered mostly when too many blocks have been mined individually. This fix will not impact worlds that have not encountered the issue but may cause some weird issues on worlds that have encountered it. It won't be game breaking or world corrupting though. If this is a significant problem, contact @drathon on discord for information on how to fix.

Patches issues with C2ME Compatibility due to multithreading by forcing thread synchronization in many places.