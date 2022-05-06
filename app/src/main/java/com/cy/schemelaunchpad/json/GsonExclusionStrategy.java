package com.cy.schemelaunchpad.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Gson忽略字段的策略
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(GsonIgnore.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}