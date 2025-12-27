package com.nexDew.Authentication.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAllPermission {
    String[] value();
}
