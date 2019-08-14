package org.realcodingteam.plan9.util;

import java.util.concurrent.TimeUnit;

public final class Time {

    public static String format(long ms) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        
        if(seconds < 60) return seconds + "s";
        
        long minutes = seconds / 60;
        if(minutes < 60) return minutes + "m " + (seconds % 60) + "s";
        
        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
    }
    
}
