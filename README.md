# 程序运行说明

程序代码已经全部放在github上，仓库地址：https://github.com/thuliangjz/dbperf，已经在ubuntu16.04和ubuntu17.10系统上测试能够正常运行。

下面的内容与在仓库中的README.md相同

- 准备

  Linux操作系统，预先安装了ant, jdk(8以上)

  注意安装jdk之后需要在`.bashrc`文件中设置`JAVA_HOME`环境变量

  本地下载了FTP服务器中插入数据的SQL文件夹

- 建立数据库

  在本地clone之后，在`dbperf`根目录下执行下面的shell命令：

  ```bash
  mkdir doc-src
  cd build
  ant hsqldb	#在build文件夹下面借助build.xml构建hsqldb的jar包,保证出现build successful之后在运行下面的指令
  ```

- 建表和数据插入

  在`dbperf`根目录下，执行如下的shell命令：

  ```bash
  cp make.sh.sample make.sh
  vim make.sh	#修改SQL_DIR指向存储SQL语句的位置，所有的.sql应该放在同一个文件夹下面，特别注意4个.sql文件名是否与make.sh中的一致，如果不一致（比如'_'和'-'的区别）需要修改
  source make.sh create	#编译生成插入数据的class(TableCreate.class和query.class)
  source start_server.sh	#启动服务器
  ```

  然后按下`Ctrl`+`C`中断server程序，这样可以在当前目录下生成一个数据库的实例的文件夹。可以看到根目录下会多一个`db0`文件夹

  使用`sed`命令修改checkpoint的延时（这一步不是必须的，只是用来使插入时间更短一些）

  ```bash
  sed -i 's/SET FILES WRITE DELAY [0-9]* MILLIS/SET FILES WRITE DELAY 500000 MILLIS/g' db0/tpc_h.script
  ```

  然后启动服务器

  ```bash
  source start_server.sh > tmp/server.log &	#在后台启动server，日志在tmp/server.log中
  ```

  编译，运行插入数据的类

  ```bash
  source make.sh compile	#编译
  source make.sh run_create	#执行建表语句
  source make.sh run_insert　> tmp/insert.log &　#执行插入语句
  ```

  最后一步执行插入操作的时候，因为插入语句非常多（860万行），即使减少了checkpoint的次数，在测试服务器上大约需要一个小时的时间，这里让它在后台运行。可以通过`tail tmp/insert.log`查看插入了多少数据。

  **写给助教学长学姐：**我们没有改动数据库文件的索引结构，所以如果有事先插入好数据的文件夹，可以直接拷贝到根目录使用，但是请保证移入的文件名称为db0，内部所有文件都以tpc_h作为开头。

- 执行查询语句：

  ```bash
  source make.sh query
  ```

  可以用来运行写好的查询类。由于冷启动时缓存为空，查询时间比较长，建议多执行几次query。最终稳定的时间大概在20s左右。

  **写给助教学长学姐：**为了方便对比，我们把测试用的剩下3种修改情形都作为单独的分支push到github上，可以切换到original（未修改），join（只修改了连接部分），iterator（只修改了迭代器部分）分支，只要重新构建数据库和执行查询语句即可。

### 