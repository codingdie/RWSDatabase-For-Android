package com.codingdie.rwsdatabase.connection;

/**
 * Created by xupeng on 2016/8/22.
 */
public class RWSColumInfo {
    private  String name;
    private  String type;
    private  boolean nullable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
