package com.example.springsecuritydemo.databind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;

public class AliasDataBinder extends ExtendedServletRequestDataBinder {

    /** 日志 */
    private final Log log = LogFactory.getLog(this.getClass());

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
                    PropertyValue propertyValue = mpvs.getPropertyValue(alias);
                    if (propertyValue == null) {
                        log.error("Got null from mpvs. It can't happen. Shut the fuck up, my IDE.");
                    } else {
                        mpvs.add(declaredField.getName(), propertyValue.getValue());
                    }
                    break;
                }
            }
        }
    }
}
