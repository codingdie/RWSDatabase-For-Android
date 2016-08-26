package com.codingdie.rwsdatabase.version.exception;

/**
 * Created by xupen on 2016/8/26.
 */
public class VersionException extends  RuntimeException {

    public  static  String NO_UPGRADE_METHOD="not implement version %d to  version %d method";
    public  static  String NO_CREATE_METHOD="not implement create database method:%d";
    public  static  String FAILED_CREATE_OR_UPGRADE_DATABASE="failed create or upgrade database";

    public VersionException(String detailMessage) {
        super(detailMessage);
    }
}
