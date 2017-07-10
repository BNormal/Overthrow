package com.example.ben.overthrow;


/**
 * Created by Ben on 7/9/2017.
 */

import java.util.Random;

public class Utils {
    private static Random random;

    public static final int random(int min, int max) {
        max = max + 1;
        final int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random(n));
    }

    public static final int random(int maxValue) {
        return (int) new Random().nextInt(maxValue);
    }
}
