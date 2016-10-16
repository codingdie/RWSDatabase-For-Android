package com.codingdie.rwsdatabase.exception;

/**
 * Created by xupeng on 2016/8/26.
 */
public class RWSOrmException extends  RuntimeException {

    public  static  String KEY_PROPERTY_IS_NULL="the inserted or updateed  object key property can't be null or 0";
    public  static  String NO_RWSTABLE_ANNOTATION="the inserted or updateed object must has RWSTable annotation  ";

    public RWSOrmException(String detailMessage) {
        super(detailMessage);
    }
}
