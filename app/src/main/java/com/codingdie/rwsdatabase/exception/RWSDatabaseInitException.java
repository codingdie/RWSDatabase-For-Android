package com.codingdie.rwsdatabase.exception;

/**
 * Created by xupeng on 2016/8/26.
 */
public class RWSDatabaseInitException extends  RuntimeException {

    public  static  String PATH_ERROR="your database path is error";

    public RWSDatabaseInitException(String detailMessage) {
        super(detailMessage);
    }
}
