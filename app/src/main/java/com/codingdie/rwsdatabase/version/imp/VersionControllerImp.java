package com.codingdie.rwsdatabase.version.imp;

import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnection;

/**
 * Created by xupen on 2016/8/25.
 */
public interface VersionControllerImp {

    public  void initDatabase(int version, Class versionManaer, SQLiteDatabase db);


}
