package com.ydl.list.treelist.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TreeDataType {
    /**
     * 要绑定的item
     */
    Class iClass() default Object.class;

    String bindField() default "";
}
