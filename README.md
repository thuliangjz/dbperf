### Prerequisites

Linux操作系统，预先安装了ant, jdk(8以上)

注意安装jdk之后需要在`.bashrc`文件中设置`JAVA_HOME`环境变量

本地下载了FTP服务器中插入数据的SQL

### 数据库build和数据插入

git clone之后，进入dbperf文件夹执行下面的shell语句

~~~bash
mkdir doc-src
cd build
ant hsqldb	#在build文件夹下面借助build.xml构建hsqldb的jar包,保证出现build successful之后在运行下面的指令
cd ..
cp make.sh.sample make.sh
vim make.sh	#修改SQL_DIR指向存储SQL语句的位置，所有的.sql应该放在同一个文件夹下面，特别注意4个.sql文件名是否与make.sh中的一致，如果不一致（比如'_'和'-'的区别）需要修改
source make.sh create	#构架插入数据的class(TableCreate.class和query.class)
source start_server.sh > tmp/server.log &	#在后台启动server，日志在tmp/server.log中
source make.sh run > tmp/insert.log & #启动插入数据的进程，在tmp/insert.log中可以看到当前插入的行数。
~~~

### 时间测试：

查询语句在query.sql（和原始版本略有区别），中当前查询时间为59s。在dbperf目录下运行`source make.sh query`即可获取查询语句的运行时间