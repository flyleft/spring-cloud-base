# saga使用教程

## pom添加依赖(版本: 0.6.0.RELEASE)

```xml
<dependencies>
     <dependency>
             <groupId>io.choerodon</groupId>
             <artifactId>choerodon-starter-core</artifactId>
             <version>${choerodon.starters.version}</version>
     </dependency>
     <dependency>
              <groupId>io.choerodon</groupId>
              <artifactId>choerodon-starter-swagger</artifactId>
              <version>${choerodon.starters.version}</version>
     </dependency>
     <dependency>
            <groupId>io.choerodon</groupId>
            <artifactId>choerodon-starter-asgard</artifactId>
            <version>${choerodon.starters.version}</version>
     </dependency>
 </dependencies>
```

## @Saga和@SagaTask的定义(请确保注解所在类可以被spring扫描到)

1. @Saga
```
@Saga(code = "asgard-create-user", description = "创建项目", inputSchema = "{}")
注解在方法或者类上。
code: 类似于kafka的topic，任务通过@SagaTask订阅，对应@SagaTask的sagaCode
description: 描述信息
inputSchema: 通过sagaClient.startSaga()的StartInstanceDTO的input的json的schema，可以不写。会提供自动生成
```

2. @SagaTask
```
@SagaTask(code = "devopsCreateUser",
        description = "devops创建用户",
        sagaCode = "asgard-create-user",
        concurrentLimitNum = 2,
        concurrentLimitPolicy = SagaDefinition.ConcurrentLimitPolicy.NONE,
        seq = 2)
注解在方法上。
code: 该task的code，同一个sagaCode下的taskCode需要唯一。
sagaCode: 对应@Saga的code，表示订阅该saga。
seq: 执行顺序，同一个saga下的task将按照seq顺序一次消费，越小消费顺序越高，上一个SagaTask的输出是上一个的输入，seq相同则并行执行，并行的任务输出的结果json进行一个merge操作。
description: 描述
maxRetryCount: 最大自动重试次数
concurrentLimitNum: 并发数，当concurrentLimitPolicy不为NONE时生效。
concurrentLimitPolicy: 并发策略，默认为NONE。TYPE根据sagaClient.startSaga时的refType设置并发，TYPE_AND_ID根据refType和ref_id设置并发，并发数为concurrentLimitNum。
一个服务将@SagaTask注解删除，asgard服务也会同步删除该SagaTask。
```

3. 自动扫描。服务添加了choerodon-starter-swagger的0.6.0版本，将自动扫描服务的@Saga和@SagaTask注解

## producer端
1. 注入一个SagaClient，通过feign调用saga。
   请确保@EnableFeignClients包含`io.choerodon.asgard.saga`，
   比如`@EnableFeignClients("io.choerodon")`, 否则扫描不到该feign。

2. 将业务代码和feign调用`sagaClient.startSaga()`放在一个事务中。

3. feign字段：
   - sagaCode: 要启用的saga的code字段，对应@Saga里的code
   - StartInstanceDTO: DTO
     1. input: 输入的json数据。
     2. userId: 方便追踪用户。DetailsHelper.getUserDetails().getUserId()传入。
     3. refType: 关联业务类型，比如project,user这些。非必须，该字段用于并发策略。
     4. refId: 关联业务类型，比如projectId,userId这些。非必须，该字段用于并发策略。
     
 4. 只用了producer没有使用consumer消费端。把`choerodon.saga.consumer.enabled`设置为false，
    这样不会创建消费端拉取消息和消息消费的bean和线程。
    
```
 @Transactional
    public AsgardUser createUser(@Valid @RequestBody AsgardUser user) {
         // 业务代码
         sagaClient.startSaga("asgard-create-user", new StartInstanceDTO(input, "", ""));
    }
```

## 消费端
```yaml
choerodon:
  saga:
    consumer:
      thread-num: 5  # 消费线程数
      poll-interval: 3 # 拉取消息的间隔(秒)，默认1秒
      max-poll-size: 200 # 每次拉取的最大消息数量
      enabled: true # 是否启用消费端
```

```
@SagaTask(code = "devopsCreateUser",
        description = "devops创建用户",
        sagaCode = "asgard-create-user",
        concurrentLimitNum = 2,
        concurrentLimitPolicy = SagaDefinition.ConcurrentLimitPolicy.NONE,
        seq = 2)
public DevopsUser devopsCreateUser(String data) throws IOException {
    AsgardUser asgardUser = objectMapper.readValue(data, AsgardUser.class);
    LOGGER.info("===== asgardUser {}", asgardUser);
    DevopsUser devopsUser = new DevopsUser();
    devopsUser.setId(asgardUser.getId());
    devopsUser.setGroup("test");
    LOGGER.info("===== devopsCreateUser {}", devopsUser);
    return devopsUser;
}
方法返回值为该任务的输出，本次sagaTask的输出是下一个sagaTsk的输入。
里面执行封装了事务，不需要再加事务，如果需要加外部事务，可通过@SagaTask的transactionDefinition设置事务传播行为。


同一个Saga下的多个SagaTask的seq相同，则并行执行。这多个SagaTask的输出进行merge后，成为下个SagaTask的输入。
merge操作的如下：
1的输出和2的输出合并：
1的code为code1，输出为{"name":"23"} 2的code为code2，输出为null 合并结果{"name":"23"}
1的code为code1，输出为{"name":"23"} 2的code为code2，输出为{"name":"23333"} 合并结果{"name":"23333"}
1的code为code1，输出为{"name":"23"} 2的code为code2，输出为{"age":23} 合并结果{"name":"23333","age":23}
1的code为code1，输出为[{"id":1},{"id":2}] 2的code为code2，输出为{"age":23} 合并结果{"code1":[{"id":1},{"id":2}],"age":23}
1的code为code1，输出为false 2的code为code2，输出为null 合并结果{"code1":false}
1的code为code1，输出为"test" 2的code为code2，输出为23 合并结果{"code1":"test","code2":23}
1的code为code1，输出为"test" 2的code为code2，输出为"23" 合并结果{"code1":"test","code2":"23"}

如果这次的输出和输入一样，直接将接收数据返回即可。
@SagaTask(code = "test", sagaCode = "iam-create-project", seq = 1)
public String iamCreateUser(String data) {
    return data;
}
```
