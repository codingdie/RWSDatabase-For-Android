package com.codingdie.rwsdatabase.exception;

/**
 * Created by xupen on 2016/8/26.
 */
public class RWSDatabaseException extends  RuntimeException {

    public  static  String PATH_ERROR="your database path is error";

    public RWSDatabaseException(String detailMessage) {
        super(detailMessage);
    }
}
