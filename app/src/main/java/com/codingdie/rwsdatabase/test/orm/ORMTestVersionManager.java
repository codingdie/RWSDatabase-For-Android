package com.codingdie.rwsdatabase.test.orm;

import com.codingdie.rwsdatabase.connection.WritableConnection;

import java.util.Random;

/**
 * Created by xupeng on 2016/8/26.
 */
public class ORMTestVersionManager {

    public void  createDatabase(WritableConnection db){
        db.execWriteSQL("DROP TABLE IF EXISTS `Class`");
        db.execWriteSQL("CREATE TABLE `Class` ( `classId`  INTEGER   ,`className`  TEXT)");
        db.execWriteSQL("DROP TABLE IF EXISTS `Student`");
        db.execWriteSQL("CREATE TABLE `Student` (`classId`  INTEGER   , `studentId`  INTEGER  ,`studentName`  TEXT)");
    }

    public void  version1ToVersion2(WritableConnection db){
        for(int i=0;i<20;i++){
            db.execWriteSQL("insertObject into   `Class` ( `classId`  ,`className`  ) values(?,?)",new Object[]{i+1,(i+1)+"班"});
            if(i==0){
                continue;
            }
            for(int j=0;j<new Random().nextInt(50)+2;j++){
                db.execWriteSQL("insertObject into   `Student` ( `classId`  ,`studentId`,`studentName`  ) values(?,?,?)",new Object[]{i+1,j+1,(i+1)+"班"+(j+1)+"号学生"});
            }
        }
    }

}


