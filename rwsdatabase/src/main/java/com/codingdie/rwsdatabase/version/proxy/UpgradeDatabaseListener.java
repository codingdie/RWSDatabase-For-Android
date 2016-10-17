package com.codingdie.rwsdatabase.version.proxy;

/**
 * Created by xupeng on 2016/10/8.
 */
public interface UpgradeDatabaseListener {

    public void beginUpgrade();

    public void endUpgrade();

    public void progress(double progress);

}
