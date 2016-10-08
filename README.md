# RWSDatabase
a read and write  separate sqlite database framework for Android with ORM,versionControl,connectionPool,threadPool  
一个带orm,版本控制,数据库连接池,线程池的读写分离的高性能的android sqlite数据库框架.

Why use this database framework?  
为什么使用这个数据库框架?

1,The only open source database framework for a multi threaded read and write separation(write does not affect the reading)  
唯一的一个实现了多线程数据库读写分离的框架(写不卡读)

2,High performance, flexible, simple and easy to use ORM mapping reference to the implementation of Mybatis framework for server  
高性能,灵活的,简单易用的ORM映射,参照了服务端最常用的mybatis框架并改进

3,Clear traceability version management module  
清晰的可追踪的版本管理

4,The only database framework to support the function of table division,  
唯一一个支持分表功能的数据库框架,分表在手机端几乎是最终的数据库查询优化方案(微信qq均已实践,一般应用不会用上)

Generally speaking, this framework is to solve the problem of building large-scale application database level, is the first choice of large scale application data framework, but may not be too suitable for small applications  
总得来说,这个框架是为了解决构建大型应用数据库层面各种问题,是大型应用数据框架首选,但可能并不太适用于小应用
***
##Installation
1,download the latest jar package from the official download into the project[[download]](wwww.codeingdie.com/RWSDatabase).  
从官网下载最新jar包导入工程[[下载]](wwww.codeingdie.com/RWSDatabase).

2,import this git project as a Android Library  
将此git工程作为Android Library引入

3,import with gradle(TODO)  
gradle引入(待做)



##Simple Usage
1,create or upgrade the database  and get the databasemanager  
创建或者升级数据库, 获取数据库管理对象
  ```
  RwsDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator( MultipleReadActivity.this)      //context
                                                        .databaseName("test1")                      //dbname
                                                        .versionManager(VersionManager.class)       //versionmanager 版本管理器
                                                        .version(2)                                  //version 版本
                                                        .connectionPoolSize(20)                      //connectionPoolSize 连接池大小
                                                        .create();       `
  ```  
  ``` 
public class VersionManager {
    
    //创建数据库(版本1)
    public void  createDatabase(WritableConnection db){
        db.execWriteSQL("DROP TABLE IF EXISTS `Class`");
        db.execWriteSQL("CREATE TABLE `Class` ( `classId`  INTEGER PRIMARY KEY  ,`className`  TEXT)");
    }
    //从版本1升级到版本2
    public void  version1ToVersion2(WritableConnection db){
        db.execWriteSQL("DROP TABLE IF EXISTS `Student`");
        db.execWriteSQL("CREATE TABLE `Student` ( `classId`  INTEGER PRIMARY KEY  ,`studentId`  INTEGER ,`studentName`  TEXT)");
        db.execWriteSQL("DROP TABLE IF EXISTS `Teacher`");
        db.execWriteSQL("CREATE TABLE `Teacher` ( `classId`  INTEGER PRIMARY KEY  ,`teacherId`  INTEGER ,`teacherName`  TEXT)");

    }
    //从版本n升级到版本n+1的变动
    public void  version(n)ToVersion(n+1)(WritableConnection db){
     
    }
}

  
 
 ```  
 2 simply query and write  with sql(no orm)
   sql查询写入(非orm)
 ``` 
  //read/query
  ReadableConnection readableConnection = rwsDatabaseManager.getReadableDatabase();
  Cursor cursor = readableConnection.execReadSQL("select sum(studentId)  from Student ", new String[]{});
  cursor.moveToNext();
  final long sum=cursor.getInt(0);
  cursor.close();
  rwsDatabaseManager.releaseReadableDatabase(readableConnection); 
     
  //write
  WritableConnection writableConnection=rwsDatabaseManager.getWritableConnection();
  writableConnection.beginTransaction();//开始事务
       
  writableConnection.execWriteSQL("insert into Student(`studentName`,`studentId`) values (?,?)", new Object[]{i, i});

  writableConnection.setTransactionSuccessful();
  writableConnection.endTransaction();;//结束事务
  rwsDatabaseManager.releaseWritableConnection();
 ```
