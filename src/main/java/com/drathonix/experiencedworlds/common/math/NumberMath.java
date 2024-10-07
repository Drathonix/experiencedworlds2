package com.drathonix.experiencedworlds.common.math;

public class NumberMath {
    public static Number add(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return n+n2.intValue();
        }
        else if(n1 instanceof Short n){
            return n+n2.shortValue();
        }
        else if(n1 instanceof Byte n){
            return n+n2.byteValue();
        }
        else if(n1 instanceof Long n){
            return n+n2.longValue();
        }
        else if(n1 instanceof Float n){
            return n+n2.floatValue();
        }
        else if(n1 instanceof Double n){
            return n+n2.doubleValue();
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number subtract(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return n-n2.intValue();
        }
        else if(n1 instanceof Short n){
            return n-n2.shortValue();
        }
        else if(n1 instanceof Byte n){
            return n-n2.byteValue();
        }
        else if(n1 instanceof Long n){
            return n-n2.longValue();
        }
        else if(n1 instanceof Float n){
            return n-n2.floatValue();
        }
        else if(n1 instanceof Double n){
            return n-n2.doubleValue();
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number multiply(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return n*n2.intValue();
        }
        else if(n1 instanceof Short n){
            return n*n2.shortValue();
        }
        else if(n1 instanceof Byte n){
            return n*n2.byteValue();
        }
        else if(n1 instanceof Long n){
            return n*n2.longValue();
        }
        else if(n1 instanceof Float n){
            return n*n2.floatValue();
        }
        else if(n1 instanceof Double n){
            return n*n2.doubleValue();
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number divide(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return n/n2.intValue();
        }
        else if(n1 instanceof Short n){
            return n/n2.shortValue();
        }
        else if(n1 instanceof Byte n){
            return n/n2.byteValue();
        }
        else if(n1 instanceof Long n){
            return n/n2.longValue();
        }
        else if(n1 instanceof Float n){
            return n/n2.floatValue();
        }
        else if(n1 instanceof Double n){
            return n/n2.doubleValue();
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number mod(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return n%n2.intValue();
        }
        else if(n1 instanceof Short n){
            return n%n2.shortValue();
        }
        else if(n1 instanceof Byte n){
            return n%n2.byteValue();
        }
        else if(n1 instanceof Long n){
            return n%n2.longValue();
        }
        else if(n1 instanceof Float n){
            return n%n2.floatValue();
        }
        else if(n1 instanceof Double n){
            return n%n2.doubleValue();
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number pow(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return Math.pow(n,n2.intValue());
        }
        else if(n1 instanceof Short n){
            return Math.pow(n,n2.shortValue());
        }
        else if(n1 instanceof Byte n){
            return Math.pow(n,n2.byteValue());
        }
        else if(n1 instanceof Long n){
            return Math.pow(n,n2.longValue());
        }
        else if(n1 instanceof Float n){
            return Math.pow(n,n2.floatValue());
        }
        else if(n1 instanceof Double n){
            return Math.pow(n,n2.doubleValue());
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number min(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return Math.min(n,n2.intValue());
        }
        else if(n1 instanceof Short n){
            return Math.min(n,n2.shortValue());
        }
        else if(n1 instanceof Byte n){
            return Math.min(n,n2.byteValue());
        }
        else if(n1 instanceof Long n){
            return Math.min(n,n2.longValue());
        }
        else if(n1 instanceof Float n){
            return Math.min(n,n2.floatValue());
        }
        else if(n1 instanceof Double n){
            return Math.min(n,n2.doubleValue());
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number max(Number n1, Number n2){
        if(n1 instanceof Integer n){
            return Math.max(n,n2.intValue());
        }
        else if(n1 instanceof Short n){
            return Math.max(n,n2.shortValue());
        }
        else if(n1 instanceof Byte n){
            return Math.max(n,n2.byteValue());
        }
        else if(n1 instanceof Long n){
            return Math.max(n,n2.longValue());
        }
        else if(n1 instanceof Float n){
            return Math.max(n,n2.floatValue());
        }
        else if(n1 instanceof Double n){
            return Math.max(n,n2.doubleValue());
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number root(Number n1, Number n2) {
        return pow(n1,divide(1.0,n2));
    }
    public static Number sqrt(Number n1) {
        return pow(n1,1.0/2.0);
    }
    public static Number ln(Number n1){
        if(n1 instanceof Integer n){
            return Math.log(n);
        }
        else if(n1 instanceof Short n){
            return Math.log(n);
        }
        else if(n1 instanceof Byte n){
            return Math.log(n);
        }
        else if(n1 instanceof Long n){
            return Math.log(n);
        }
        else if(n1 instanceof Float n){
            return Math.log(n);
        }
        else if(n1 instanceof Double n){
            return Math.log(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number log10(Number n1){
        if(n1 instanceof Integer n){
            return Math.log10(n);
        }
        else if(n1 instanceof Short n){
            return Math.log10(n);
        }
        else if(n1 instanceof Byte n){
            return Math.log10(n);
        }
        else if(n1 instanceof Long n){
            return Math.log10(n);
        }
        else if(n1 instanceof Float n){
            return Math.log10(n);
        }
        else if(n1 instanceof Double n){
            return Math.log10(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number ln1p(Number n1){
        if(n1 instanceof Integer n){
            return Math.log1p(n);
        }
        else if(n1 instanceof Short n){
            return Math.log1p(n);
        }
        else if(n1 instanceof Byte n){
            return Math.log1p(n);
        }
        else if(n1 instanceof Long n){
            return Math.log1p(n);
        }
        else if(n1 instanceof Float n){
            return Math.log1p(n);
        }
        else if(n1 instanceof Double n){
            return Math.log1p(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number floor(Number n1){
        if(n1 instanceof Integer n){
            return Math.floor(n);
        }
        else if(n1 instanceof Short n){
            return Math.floor(n);
        }
        else if(n1 instanceof Byte n){
            return Math.floor(n);
        }
        else if(n1 instanceof Long n){
            return Math.floor(n);
        }
        else if(n1 instanceof Float n){
            return Math.floor(n);
        }
        else if(n1 instanceof Double n){
            return Math.floor(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number ceil(Number n1){
        if(n1 instanceof Integer n){
            return Math.ceil(n);
        }
        else if(n1 instanceof Short n){
            return Math.ceil(n);
        }
        else if(n1 instanceof Byte n){
            return Math.ceil(n);
        }
        else if(n1 instanceof Long n){
            return Math.ceil(n);
        }
        else if(n1 instanceof Float n){
            return Math.ceil(n);
        }
        else if(n1 instanceof Double n){
            return Math.ceil(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number round(Number n1){
        if(n1 instanceof Integer n){
            return Math.round(n);
        }
        else if(n1 instanceof Short n){
            return Math.round(n);
        }
        else if(n1 instanceof Byte n){
            return Math.round(n);
        }
        else if(n1 instanceof Long n){
            return Math.round(n);
        }
        else if(n1 instanceof Float n){
            return Math.round(n);
        }
        else if(n1 instanceof Double n){
            return Math.round(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number abs(Number n1){
        if(n1 instanceof Integer n){
            return Math.abs(n);
        }
        else if(n1 instanceof Short n){
            return Math.abs(n);
        }
        else if(n1 instanceof Byte n){
            return Math.abs(n);
        }
        else if(n1 instanceof Long n){
            return Math.abs(n);
        }
        else if(n1 instanceof Float n){
            return Math.abs(n);
        }
        else if(n1 instanceof Double n){
            return Math.abs(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number exp(Number n1){
        if(n1 instanceof Integer n){
            return Math.exp(n);
        }
        else if(n1 instanceof Short n){
            return Math.exp(n);
        }
        else if(n1 instanceof Byte n){
            return Math.exp(n);
        }
        else if(n1 instanceof Long n){
            return Math.exp(n);
        }
        else if(n1 instanceof Float n){
            return Math.exp(n);
        }
        else if(n1 instanceof Double n){
            return Math.exp(n);
        }
        throw new IllegalStateException("Impossible NaN");
    }
    public static Number summate(Number k, Number d1, Number dk){
        if(d1 instanceof Integer n){
            return k.intValue()*(n+dk.intValue())/2;
        }
        else if(d1 instanceof Short n){
            return k.shortValue()*(n+dk.shortValue())/2;
        }
        else if(d1 instanceof Byte n){
            return k.byteValue()*(n+dk.byteValue())/2;
        }
        else if(d1 instanceof Long n){
            return k.longValue()*(n+dk.longValue())/2;
        }
        else if(d1 instanceof Float n){
            return k.floatValue()*(n+dk.floatValue())/2;
        }
        else if(d1 instanceof Double n){
            return k.doubleValue()*(n+dk.doubleValue())/2;
        }
        throw new IllegalStateException("Impossible NaN");
    }
}
