package com.codingdie.rwsdatabase.version.imp;

import com.codingdie.rwsdatabase.connection.WritableConnection;

/**
 * Created by xupen on 2016/8/25.
 */
public interface VersionControllerImp {

    public  void createOrUpgradeDatabase(int version, Class versionManaer, WritableConnection db);


}
