学习记录：
======
一、Nacos
-----
### 安装
        git clone https://github.com/alibaba/nacos.git
        cd nacos/
        mvn -Prelease-nacos -Dmaven.test.skip -Drat.skip=true clean install -U 
        ls -al distribution/target/
        修改$version字段，改成你对应的版本号
        cd distribution/target/nacos-server-$version/nacos/bin
        默认8848端口，127.0.0.1:8848打开console
        配置命名空间
        配置Data Id
        版本为1.4.0-SNAPSHOT
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
        **注意**：出现nacos:failed to req API:/nacos/v1/ns/instance错误，可能是因为本机ip发生变化，可修改cluster.conf为当前本机ip
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

三、Sentinel
-----
### 下载
        本Demo使用的sentinel版本为1.7.2，点击链接下载：https://github.com/alibaba/Sentinel/archive/1.7.2.zip
        或在https://github.com/alibaba/Sentinel/releases选择合适的版本

### 改造微服务
#### 1、新增依赖
        <dependency>
          <groupId>com.alibaba.csp</groupId>
          <artifactId>sentinel-datasource-nacos</artifactId>
          <version>1.5.2</version>
        </dependency>
        
        <dependency>
          <groupId>com.alibaba.cloud</groupId>
          <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        
#### 2、修改配置文件
        bootstrap.yml 配置sentinel的数据源
        spring:
          cloud: 
            sentinel:
              transport:
                dashboard: 127.0.0.1:8088
              datasource:
                flow:
                  nacos:
                    server-addr: 127.0.0.1:8848
                    dataId: ${spring.application.name}-flow-rules
                    rule-type: flow
                    groupId: SENTINEL_GROUP
                    data-type: json
                    namespace: 66b1e88d-4935-4f81-b948-7a492f794d7c
                degrade:
                  nacos:
                    server-addr: 127.0.0.1:8848
                    dataId: ${spring.application.name}-degrade-rules
                    rule-type: degrade
                    groupId: SENTINEL_GROUP
                    data-type: json
                    namespace: 66b1e88d-4935-4f81-b948-7a492f794d7c
                    
        application.yml 启用feign sentinel
        feign:
          sentinel:
            enabled: true

#### 3、使用方式
        官方示例：https://github.com/alibaba/Sentinel/blob/1.7.2/sentinel-extension/sentinel-annotation-aspectj/src/test/java/com/alibaba/csp/sentinel/annotation/aspectj/integration/service/FooService.java
        属性总结：
        value：（必填）资源名称
        entryType：（非必填）entry类型，标记流量的方向，取值IN/OUT，默认是OUT
        blockHandler：（非必填）处理BlockException的函数名称。函数要求：
        1. 必须是 public
        2. 返回类型与原方法一致
        3. 参数类型需要和原方法相匹配，并在最后加 BlockException 类型的参数。
        4. 默认需和原方法在同一个类中。若希望使用其他类的函数，可配置 blockHandlerClass ，并指定blockHandlerClass里面的方法。
        blockHandlerClass：（非必填）存放blockHandler的类。对应的处理函数必须static修饰，否则无法解析，其他要求：同blockHandler。
        fallback：（非必填）用于在抛出异常的时候提供fallback处理逻辑。fallback函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。函数要求：
        1. 返回类型与原方法一致
        2. 参数类型需要和原方法相匹配，Sentinel 1.6开始，也可在方法最后加 Throwable 类型的参数。
        3.默认需和原方法在同一个类中。若希望使用其他类的函数，可配置 fallbackClass ，并指定fallbackClass里面的方法。
        fallbackClass：（非必填）存放fallback的类。对应的处理函数必须static修饰，否则无法解析，其他要求：同fallback。
        defaultFallback：（非必填）用于通用的 fallback 逻辑。默认fallback函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。若同时配置了 fallback 和 defaultFallback，以fallback为准。函数要求：
        1. 返回类型与原方法一致
        2. 方法参数列表为空，或者有一个 Throwable 类型的参数。
        3. 默认需要和原方法在同一个类中。若希望使用其他类的函数，可配置 fallbackClass ，并指定 fallbackClass 里面的方法。
        exceptionsToIgnore：（非必填）指定排除掉哪些异常。排除的异常不会计入异常统计，也不会进入fallback逻辑，而是原样抛出

### 改造Sentinel
        对sentinel-dashboard模块进行修改：
        1、对pom.xml进行修改
          <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
            <scope>test</scope>
          </dependency>
        将<scope>test</scope>去掉
          <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
          </dependency>
        2、修改java代码
        sentinel-dashboard/src/test/java/com/alibaba/csp/sentinel/dashboard/rule/nacos
        整个目录拷贝到
        sentinel-dashboard/src/main/java/com/alibaba/csp/sentinel/dashboard/rule/nacos
        
        修改/Sentinel-1.7.2/sentinel-dashboard/src/main/java/com/alibaba/csp/sentinel/dashboard/controller/v2/FlowControllerV2.java文件
            // @Autowired
            // @Qualifier("flowRuleDefaultProvider")
            // private DynamicRuleProvider<List<FlowRuleEntity>> ruleProvider;
            // @Autowired
            // @Qualifier("flowRuleDefaultPublisher")
            // private DynamicRulePublisher<List<FlowRuleEntity>> rulePublisher;
        
            @Autowired
            @Qualifier("flowRuleNacosProvider")
            private DynamicRuleProvider<List<FlowRuleEntity>> ruleProvider;
            @Autowired
            @Qualifier("flowRuleNacosPublisher")
            private DynamicRulePublisher<List<FlowRuleEntity>> rulePublisher;
            
        修改/Sentinel-1.7.2/sentinel-dashboard/src/main/webapp/resources/app/scripts/directives/sidebar/sidebar.html
        将已注释的流控规则 V1 打开
        <li ui-sref-active="active" ng-if="entry.appType==0">
            <a ui-sref="dashboard.flow({app: entry.app})">
                <i class="glyphicon glyphicon-filter"></i>&nbsp;&nbsp;流控规则 V1
            </a>
        </li>
       
        **注意**：如果nacos配置中心与sentinel配置中心不在同一ip，则需要在/Sentinel-1.7.2/sentinel-dashboard/src/main/java/com/alibaba/csp/sentinel/dashboard/rule/nacos/NacosConfig.java修改
        nacosConfigService方法：
        Properties properties = new Properties();
        // nacos配置地址，默认localhost，如果在别的服务器上则需要配置ip+端口
        properties.put(PropertyKeyConst.SERVER_ADDR, "localhost");
        properties.put(PropertyKeyConst.NAMESPACE, "66b1e88d-4935-4f81-b948-7a492f794d7c");
        return ConfigFactory.createConfigService(properties);

### 启动Sentinel
        1、编译生成jar包：执行命令mvn clean package -DskipTests
        2、进入sentinel-dashboard模块的target目录执行命令 java -Dserver.port=8088 -Dcsp.sentinel.dashboard.server=localhost:8088 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
        3、打开http://127.0.0.1:8088/，用户名密码默认都为sentinel，可用命令修改：
        -Dsentinel.dashboard.auth.username=sentinel # 设置登录的帐号为：sentinel
        -Dsentinel.dashboard.auth.password=123456 # 设置登录的密码为：123456

### 配置nacos
        新增流控规则dataId，命名为${spring.application.name}-flow-rules，数据格式为json
        resource：资源名称
        limitApp：来源应用
        grade：阀值类型，0：线程数，1：QPS
        count：单机阀值
        strategy：流控模式，0：直接，1：关联，2：链路
        controlBehavior：流控效果，0：快速失败，1：warmUp，2：排队等待
        clusterMode：是否集群
        例：
        [
            {
                "resource": "/provider",
                "limitApp": "default",
                "grade": 1,
                "count": 1,
                "strategy": 0,
                "controlBehavior": 0,
                "clusterMode": false
            },
            {
                "resource": "/test",
                "limitApp": "default",
                "grade": 1,
                "count": 10,
                "strategy": 0,
                "controlBehavior": 2,
                "clusterMode": false
            }
        ]
        新增降级规则dataId，命名为${spring.application.name}-degrade-rules，数据格式为json
        resource：资源名称
        grade：降级策略 0 RT 1 异常⽐例 2 异常数
        count：阈值
        timeWindow：时间窗
        例：
        [
        	 {
        		"resource":"/test",
        		"grade":2,
        		"count":0,
        		"timeWindow":5
        	 }
        ]
        
        **微服务sentinel.nacos相关配置要与所创建的dataid相关信息一致**
        
### 当前版本nacos + sentinel持久化实现注意
        1、Sentinel控制台中修改规则，仅是内存中⽣效，不会修改Nacos中的配置值，重启后恢复原来的值。
        2、Nacos控制台中修改规则，不仅内存中⽣效，Nacos中持久化规则也⽣效，重启后规则依然保持。
        
        更多细节请查看：https://github.com/alibaba/Sentinel/wiki