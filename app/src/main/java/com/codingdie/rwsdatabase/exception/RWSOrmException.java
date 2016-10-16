package com.codingdie.rwsdatabase.exception;

/**
 * Created by xupen on 2016/8/26.
 */
public class RWSOrmException extends  RuntimeException {

    public  static  String KEY_PROPERTY_IS_NULL="inert object key property can't be null or 0";

    public RWSOrmException(String detailMessage) {
        super(detailMessage);
    }
}
