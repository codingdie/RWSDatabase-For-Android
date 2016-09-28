package com.codingdie.rwsdatabase.orm.annotation;

/**
 * Created by xupen on 2016/8/22.
 */
public @interface ColumAnnotation {
    public  String[] alias()    ;
    public  boolean ignore() default  false    ;
    public  boolean isKey() default  false;

}
