package com.ydl.list.treelist.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by baozi on 2018/2/27.
 * 如果使用在item的类上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TreeItemType {
    /**
     * 映射的type值
     *
     * @return [type1, type2]
     */
    String[] type();

    /**
     * item的列占比
     *
     * @return 默认返回0, 也就是不设置
     */
    int spanSize() default 0;
}
