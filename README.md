学习记录：
======
一、Nacos
-----
### 安装
        git clone https://github.com/alibaba/nacos.git
        cd nacos/
        mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U  
        ls -al distribution/target/修改$version字段，改成你对应的版本号
        cd distribution/target/nacos-server-$version/nacos/bin
        默认8848端口，127.0.0.1:8848打开console
        配置命名空间
        配置Data Id
### 关闭服务
        sh shutdown.sh
### 开启服务（单机）
        bash startup.sh -m standalone
### 开启服务（集群）
        路径：/nacos/distribution/target/nacos-server-1.4.0-SNAPSHOT/nacos/conf下
        文件application.properties配置mysql相关信息（目前只能用mysql数据库）
        在mysql中运行nacos-mysql.sql文件
        新建cluster.conf文件，内容如下：
                #ip:port
                111.111.0.11:8848
                222.222.0.22:8848
        启动命令：sh startup.sh