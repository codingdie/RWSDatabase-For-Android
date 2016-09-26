# RWSDatabase
a read and write  separate sqlite database framework for Android with ORM,versionControl,connectionPool,threadPool (一个带orm,版本控制,数据库连接池,线程池的读写分离的高性能的android sqlite数据库框架)

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