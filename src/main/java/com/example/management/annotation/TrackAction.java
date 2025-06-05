package com.example.management.annotation;

import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackAction {

    AuditAction action();

    EntityType entityType();

    String entityId() default "";

    String entityName() default "";

    boolean enabled() default true;
}
