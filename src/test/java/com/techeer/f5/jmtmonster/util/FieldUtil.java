package com.techeer.f5.jmtmonster.util;

import java.lang.reflect.Field;

public class FieldUtil {

    public static Object writeField(Object instance, String fieldName, Object fieldValue) {
        try {
            Field instanceField = instance.getClass().getDeclaredField(fieldName);
            instanceField.setAccessible(true);
            instanceField.set(instance, fieldValue);
            return instance;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No such field named " + fieldName);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot set the field with given value");
        }
    }
}
