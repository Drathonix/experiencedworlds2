package com.drathonix.serverstatistics.common.storage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;

public class FakeFile extends File {
    public FakeFile(@NotNull String pathname) {
        super(pathname);
    }

    public FakeFile(String parent, @NotNull String child) {
        super(parent, child);
    }

    public FakeFile(File parent, @NotNull String child) {
        super(parent, child);
    }

    public FakeFile(@NotNull URI uri) {
        super(uri);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
