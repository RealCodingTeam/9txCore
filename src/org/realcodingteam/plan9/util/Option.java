package org.realcodingteam.plan9.util;

import java.util.Optional;
import java.util.function.Consumer;

public final class Option {
    
    /**
     * Convenience class for dealing with Optionals in a jdk 8 style.
     * This should be in Optional, but I guess that'd be too powerful...
     */
    public static <T> void ifPresentOrElse(Optional<T> optional, Consumer<? super T> consumer, Runnable absent) {
        if(optional.isPresent()) consumer.accept(optional.get());
        else absent.run();
    }
    
}
