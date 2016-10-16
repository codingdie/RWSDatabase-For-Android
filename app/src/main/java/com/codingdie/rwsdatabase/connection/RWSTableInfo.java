package com.codingdie.rwsdatabase.connection;

import java.util.List;

/**
 * Created by xupen on 2016/8/22.
 */
public  class RWSTableInfo {
    private  String name;
    private List<RWSColumInfo> colums;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RWSColumInfo> getColums() {
        return colums;
    }

    public void setColums(List<RWSColumInfo> colums) {
        this.colums = colums;
    }
}
