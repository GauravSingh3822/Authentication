package com.nexDew.Authentication.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyAuthority(#root.this.getPermission())")
public @interface RequireAnyPermission {
    String[] value();
}
