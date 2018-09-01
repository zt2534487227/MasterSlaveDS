package com.zt.masterSlaveDS.aspect;

import com.zt.masterSlaveDS.annotation.TargetDataSource;
import com.zt.masterSlaveDS.datasource.DynamicDataSource;
import com.zt.masterSlaveDS.entity.DSType;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @Author: ZhouTian
 * @Description:
 * @Date: 2018/7/10
 */
public class DynamicDataSourceAdvice implements MethodBeforeAdvice,AfterReturningAdvice {

    private DynamicDataSource dynamicDataSource;

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        String methodName = method.getName();
        DSType dataSource = DSType.WRITE;
        if (methodName.startsWith("find") || methodName.startsWith("get") || methodName.startsWith("search")
                || methodName.startsWith("select") || methodName.startsWith("query")
                || methodName.startsWith("list") || methodName.startsWith("count")) {
            dataSource = DSType.READ;
        }
        try {
            if (clazz.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource annotation = clazz.getAnnotation(TargetDataSource.class);
                dataSource = annotation.value();
            }
            if (method.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource annotation = method.getAnnotation(TargetDataSource.class);
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamicDataSource.setDS(dataSource);
    }

    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        dynamicDataSource.clearDS();
    }

    public DynamicDataSource getDynamicDataSource() {
        return dynamicDataSource;
    }

    public void setDynamicDataSource(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }
}
