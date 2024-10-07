package com.drathonix.experiencedworlds.common.data;

public interface IWorldBorderData {
    int getExpansions();
    void expand(int expansions);
    double getTransformedBorderSize();
}
