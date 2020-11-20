学习记录：
======
一、Nacos
-----
### 安装
        git clone https://github.com/alibaba/nacos.git
        cd nacos/
        mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U  
        ls -al distribution/target/
        修改$version字段，改成你对应的版本号
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
### 服务注册
        data id：
          由{spring.application.name}-{spring.profiles.active}.{spring.cloud.nacos.config.file-extension}组成
        配置内容（例）：
          server:
            port: 10000
        openFeign：
          @FeignClient(name = "alibaba-cloud-provider")
          name为provider的{spring.application.name}
          
二、dynamic多数据源配置
-----
### pom
        <dependency>
           <groupId>com.baomidou</groupId>
           <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
           <version>${dynamic.version}/2.5.4</version>
        </dependency>
        
### yml
        spring:
          datasource:
            type: com.alibaba.druid.pool.DruidDataSource
            dynamic:
              primary: master
              datasource:
                master:
                  url: jdbc:mysql://127.0.0.1:3306/test?setUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true
                  username: root
                  password: root123456
                  driver-class-name: com.mysql.jdbc.Driver
                #          schema: db/migration/V20201113__mi05_mi17.sql # 配置则生效,自动初始化表结构
                #          data: db/migration/V20201113__mi05_mi17.sql # 配置则生效,自动初始化数据
                #          continue-on-error: false # 默认true,初始化失败是否继续
                #          separator: ";" # sql默认分号分隔符
                slave:
                  url: jdbc:mysql://127.0.0.1:3306/slave?setUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true
                  username: root
                  password: root123456
                  driver-class-name: com.mysql.jdbc.Driver
                  
### 学习心得
        1、使用@DS注解指定数据源（默认使用primary指定的数据源）
        2、使用原生spring事务会导致@DS注解失效，支持基于seata的分布式事务方案（基于读写分离，默认使用主库写并不会影响使用）
        3、方法注解优先于类上注解
        4、主从库配置完成后即可完成读写分离
        5、启动类需要声明@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})，因为spring默认使用DataSourceAutoConfigure.class配置类加载数据源