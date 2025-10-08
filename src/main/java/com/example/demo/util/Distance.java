package com.example.demo.util;

public class Distance {

    public static String toVectorLiteral(float[] v) {
        StringBuilder sb = new StringBuilder(v.length * 8);
        sb.append('[');
        for (int i = 0; i < v.length; i++) {
            if (i > 0) sb.append(',');
            // избегаем экспоненциальной нотации
            sb.append(Float.toString(v[i]));
        }
        sb.append(']');
        return sb.toString();
    }
}