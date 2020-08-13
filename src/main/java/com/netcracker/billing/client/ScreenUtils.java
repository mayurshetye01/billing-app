package com.netcracker.billing.client;

public abstract class ScreenUtils {

    public static boolean isYes(String str) {
        return str.equalsIgnoreCase("Y") || str.equalsIgnoreCase("Yes");
    }
}
