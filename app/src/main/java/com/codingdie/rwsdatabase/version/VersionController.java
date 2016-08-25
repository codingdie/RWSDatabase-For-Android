package com.codingdie.rwsdatabase.version;

import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.version.imp.VersionControllerImp;

/**
 * Created by xupen on 2016/8/25.
 */
public class VersionController implements VersionControllerImp {
    @Override
    public void initDatabase(int version, Class versionManaer, SQLiteDatabase db) {

    }

    private  void  getCurrentVersion(SQLiteDatabase db){

    }
}
