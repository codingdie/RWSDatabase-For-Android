package com.codingdie.rwsdatabase.version.imp;

/**
 * Created by xupen on 2016/10/8.
 */
public interface UpgradeDatabaseListener {
    public void beginUpgrade();
    public void endUpgrade();
    public void progress(double progress);

}