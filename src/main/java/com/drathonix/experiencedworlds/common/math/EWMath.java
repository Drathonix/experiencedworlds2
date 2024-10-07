package com.drathonix.experiencedworlds.common.math;

import com.drathonix.experiencedworlds.common.config.EWCFG;

public class EWMath {
    public static double summate(int n, double d1, double dn){
        return n*(d1+dn)/2.0;
    }
    public static double baseToTheX(double base, double x, double yshift){
        if(base < 1) base++;
        return Math.pow(base,x)+yshift;
    }
    public static double logBase(int base, int n){
        return Math.log(n)/Math.log(base);
    }

    public static double logConfigBase(int n) {
        return logBase(EWCFG.gameplay.logBase,n);
    }
}
