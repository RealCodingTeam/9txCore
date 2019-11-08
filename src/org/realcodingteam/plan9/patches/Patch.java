package org.realcodingteam.plan9.patches;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Patch {
    String internal_name();
    String display_name();
    String description() default "No description provided";
    String author() default "No author provided";
}
