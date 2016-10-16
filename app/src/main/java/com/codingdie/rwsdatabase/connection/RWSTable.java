package com.codingdie.rwsdatabase.connection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by xupen on 2016/8/22.
 */
class RWSTable {
    private  String name;
    private List<RWSColum> colums;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RWSColum> getColums() {
        return colums;
    }

    public void setColums(List<RWSColum> colums) {
        this.colums = colums;
    }
}
