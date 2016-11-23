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

4,The only database framework to support the function of RWSTable division,
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

4,源码引进(非test目录)

##Simple Usage
1,create or upgrade the database  and get the databasemanager  
创建或者升级数据库, 获取数据库管理对象
  ```
  RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator( MultipleReadActivity.this)      //context
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
        db.execWriteSQL("CREATE TABLE `Class` ( `classId`  INTEGER   ,`className`  TEXT)");
        db.execWriteSQL("DROP TABLE IF EXISTS `Student`");
        db.execWriteSQL("CREATE TABLE `Student` (`classId`  INTEGER   , `studentId`  INTEGER  ,`studentName`  TEXT)");
    }
    //从版本1升级到版本2
    public void  version1ToVersion2(WritableConnection db){
      for(int i=0;i<20;i++){
            db.execWriteSQL("insert into   `Class` ( `classId`  ,`className`  ) values(?,?)",new Object[]{i+1,(i+1)+"班"});
            if(i==0){
                continue;
            }
            for(int j=0;j<new Random().nextInt(50)+2;j++){
                db.execWriteSQL("insert into   `Student` ( `classId`  ,`studentId`,`studentName`  ) values(?,?,?)",new Object[]{i+1,j+1,(i+1)+"班"+(j+1)+"号学生"});
            }
        }
    }
    //从版本n升级到版本n+1的变动
    public void  version(n)ToVersion(n+1)(WritableConnection db){
     
    }
}

  
 ```  
 2 simply query and write  with orm
 ORM查询写入
 ``` 
  假设有张数据表Student(studentId,studentName)对应下面这个类
  @RWSTable(name = "Student") 注解表示这个类属于哪张表
  public class Student {

    @RWSColum(isKey = true)  //注解表示这个字段对应数据主键
    private  int studentId;
    private  String studentName;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
Student student
插入：
        rwsDatabaseManager.execWriteOperator(new WriteOperator() {
            @Override
            public void exec(WritableConnection writableConnection) {
                writableConnection.insert(student);
            }
        });
更新
    rwsDatabaseManager.execWriteOperator(new WriteOperator() {
            @Override
            public void exec(WritableConnection writableConnection) {
                writableConnection.update(student);
            }
        });
查询单个Student
     Student student = rwsDatabaseManager.execReadOperator(new ReadOperator<Student>() {
            @Override
            public Student exec(ReadableConnection readableConnection) {
                return readableConnection.queryObject("select * from Student where studentId=20", new String[]{}, Student.class);
            }
        });
查询列表Student
  List<Student> students= rwsDatabaseManager.execReadOperator(new ReadOperator<List<Student>>() {
            @Override
            public List<Student> exec(ReadableConnection readableConnection) {
                return readableConnection.queryObjectList("select * from Student", new String[]{}, Student.class);
            }
        });
 ```
以上为简单用法,但实际框架非常灵活强大,支持以下特性:  
1,不需要提前建立关系就支持级联查询一对（多个）多,比如查询一个班级信息连带所有的学生信息和所有老师信息  

2,默认映射规则是数据库字段名对应对象名，但用注解支持多个别名机制(@RWSColum(isKey = true,alias = {"id","keyId"}))  

3,渐进式版本管理是基础功能,在此基础上提供从任意配置版本开始的渐进式版本管理，提高效能  

4,读写完全分离,如果一个线程在写入，这个时候另外一个线程准备查询,普通的数据库框架是不能并发(可以自己测试),而我这个完全互不相印象。理论基础是wal模式和多连接(2个链接可以做到读写并发)  
  
5,自动化的事务异常处理,不需要过于关心事务,同时特殊需求的话也可以自己手动管理  

6,后期准备支持内存二级缓存和基于注解的自动化分表  
  
更多细节API之后会整理详细文档

 
