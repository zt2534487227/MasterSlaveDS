package com.zt.masterSlaveDS.annotation;

import com.zt.masterSlaveDS.entity.DSType;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    DSType value() default DSType.WRITE;
}
