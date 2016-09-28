package com.codingdie.rwsdatabase.orm.cache.model;

import java.util.List;

/**
 * Created by xupen on 2016/9/28.
 */
public class ClassInfo {

    private List<PropertyInfo> properties;
    private List<Integer> keyPropertyIndexes;

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }

    public List<Integer> getKeyPropertyIndexes() {
        return keyPropertyIndexes;
    }

    public void setKeyPropertyIndexes(List<Integer> keyPropertyIndexes) {
        this.keyPropertyIndexes = keyPropertyIndexes;
    }
}