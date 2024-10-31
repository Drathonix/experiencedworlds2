package com.drathonix.serverstatistics.common.bridge;

import java.io.File;

public interface IMixinDimensionDataStorage {
    void ss$forceSave(String key);
    File ss$getCustomDataFile(String name);
}
