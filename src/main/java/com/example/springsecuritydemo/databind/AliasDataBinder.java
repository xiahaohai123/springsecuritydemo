package com.example.springsecuritydemo.databind;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;

public class AliasDataBinder extends ExtendedServletRequestDataBinder {

    public AliasDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);
        Object target = getTarget();
        if (target == null) {
            return;
        }
        Class<?> targetClass = target.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            AliasProperty annotation = declaredField.getAnnotation(AliasProperty.class);
            if (mpvs.contains(declaredField.getName()) || annotation == null) {
                continue;
            }
            for (String alias : annotation.alias()) {
                if (mpvs.contains(alias)) {
                    mpvs.add(declaredField.getName(), mpvs.getPropertyValue(alias).getValue());
                    break;
                }
            }
        }
    }
}
