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
        

