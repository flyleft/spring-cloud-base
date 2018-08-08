# saga使用教程

## pom添加依赖(版本: 0.6.0.RELEASE)

### 如果本地有starter包缓存，最好手动删除maven仓库下的io/choerodon的jar包，特别为core,swagger和asgard。

```xml
<dependencies>
     <dependency>
            <groupId>io.choerodon</groupId>
            <artifactId>choerodon-starter-asgard</artifactId>
            <version>${choerodon.starters.version}</version>
     </dependency>
 </dependencies>
```

## @Saga和@SagaTask的定义(请确保注解所在类可以被spring扫描到)

- @Saga
```
@Saga(code = "asgard-create-user", description = "创建项目", inputSchemaClass = AsgardUser.class)
注解在方法或者类上。
code: 类似于kafka的topic，任务通过@SagaTask订阅，对应@SagaTask的sagaCode
description: 描述信息
inputSchema: 该saga输入的demo,比如{"name":"string", "age":0}。会覆盖inputSchemaClass自动生成。
inputSchemaClass: 指定class自动生成。比如指定AsgardUser将自动生成{"id":0,"username":"string","password":"string"}。
```

- @SagaTask
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
outputSchemaClass: 默认将@SagaTask的返回类型生成输出，也可通过此属性指定。
transactionTimeout: 事务超时时间，默认用不超时
transactionReadOnly: 是否为只读事务
transactionIsolation: 事务的隔离级别
transactionManager: 使用的事务管理器
```

- 自动扫描，扫描规则如下
```
在服务启动后`asgard-service`会主动拉取@Saga和@SagaTask的注解配置:
不存在则插入；
存在则更新；
原本存在后来删除注解，SagaTask会删除，Saga不做处理。
```

## producer端
- 注入一个SagaClient，通过feign调用saga。
   请确保@EnableFeignClients包含`io.choerodon.asgard.saga`，
   比如`@EnableFeignClients("io.choerodon")`, 否则扫描不到该feign。

- 将业务代码和feign调用`sagaClient.startSaga()`放在一个事务中。

- feign字段：
   - sagaCode: 要启用的saga的code字段，对应@Saga里的code
   - StartInstanceDTO: DTO
     1. input: 输入的json数据。
     2. userId: 方便追踪用户。DetailsHelper.getUserDetails().getUserId()传入。
     3. refType: 关联业务类型，比如project,user这些。非必须，该字段用于并发策略。
     4. refId: 关联业务类型，比如projectId,userId这些。非必须，该字段用于并发策略。
     
- 只用了producer没有使用consumer消费端。把`choerodon.saga.consumer.enabled`设置为false，
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
      thread-num: 5 # saga消息消费线程池大小
      max-poll-size: 200 # 每次拉取消息最大数量
      enabled: true # 启动消费端
      poll-interval-ms: 1000 # 拉取间隔，默认1000毫秒
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
1的输出结果和2的完全相同，则合并结果为1或者2的输出。
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
但这样默认根据方法返回值即'String'生成的outputChema是错误的，最好手动指定，即:
@SagaTask(code = "test", sagaCode = "iam-create-project", seq = 1,  outputSchemaClass = AsgardUser.class)
public String iamCreateUser(String data) {
    return data;
}
或者指定正确的返回值
@SagaTask(code = "test", sagaCode = "iam-create-project", seq = 1)
public AsgardUser iamCreateUser(String data) {
    AsgardUser asgardUser = objectMapper.readValue(data, AsgardUser.class);
    return asgardUser;
}
```

## 大致原理
为了确保消息的一致性，应该确保让@SagaTask注解的方法按照seq顺序依次消费，并确保消息务必被消费且仅被消费一次或者异常全部回滚。

### asgard-service服务拉取端

```
防止消费端多实例拉取出现消费，对每条消息设置一个实例锁，
当消息实例锁为空时，消费端拉取该条消息并更新实例锁，更新成功，则拉取可以成功；
当消息实例锁不为空时，查询消息实例是否为拉取的消费端实例，是则允许拉取不是则不允许拉取该条消息。

为防止多实例拉取影响，对拉取代码加锁，锁为sagaCode + taskCode
StringLockProvider.Mutex mutex = stringLockProvider.getMutex(code.getSagaCode() + ":" + code.getTaskCode());
synchronized (mutex) {
}

并发策略为TYPE_AND_ID或者为TYPE的消息，按创建id排序，每次只可以取到@SagaTask设置的concurrentLimitNum数目。
```

### 消费端

#### 消费端模型
```
一个定时任务线程定时拉取消息，拉取的消息放到一个线程安全的set里，再由消息消费线程池异步消费，每消费完成(无论成功还是失败)set从中删除，
知道set为空再进行下一次拉取消费。
```

#### 消费端事务

```
1. @SagaTask注解的方法封装了事务，有如下事务属性可配置：
transactionTimeout: 事务超时时间，默认用不超时
transactionReadOnly: 是否为只读事务
transactionIsolation: 事务的隔离级别
transactionManager: 使用的事务管理器

2. 如果@SagaTask方法里面自己又添加了事务，则形成嵌套事务，自己添加的事务设置合适的事务传播行为即可。

3. @SagaTask的方法执行遇到任何异常都会回滚事务，如果无需回滚，则手动捕获该异常即可,如下：
 @SagaTask(code = "book-tour-hotel",
            description = "预定酒店",
            sagaCode = "book-tour-package",
            concurrentLimitNum = 2,
            seq = 5)
    public TourDTO bookHotel(String data) throws IOException {
        TourDTO tour = mapper.readValue(data, TourDTO.class);
        TourHotel hotel = new TourHotel();
        hotel.setUserId(tour.getUserId());
        hotel.setTourId(tour.getTourId());
        if (tourHotelMapper.insert(hotel) != 1) {
            throw new CommonException("error.tour.bookHotel");
        }
        tour.setHotelId(hotel.getId());
        //比如该feign做一些清理，成功与否无关紧要，则可以手动捕获该异常。
       try {
           XXXFeign.cleanup(tour.getUserId());
       } catch (Exception e) {
           LOGGER
       }
        return tour;
    }

4. @SagaTask的方法里含有feign调用, 最好能保证feign调用的"幂等性"
   @SagaTask的方法里有：
   (1) db操作一
   (2) feign调用一
   (3) feign调用二
   (4) db操作二
   
   如果一次操作过程中: 1、2成功，3失败，则事务回滚，但是"feign调用一"已经执行成功，如果下一次重试"feign调用一"又会调用，
   如果"feign调用一"的接口为"非幂等性"接口，如创建项目往往出现问题，因此此时可以先手动查询，不存在再创建，保证feign调用"幂等性"
```

#### 消费端一致性
```
消费端原理：

1. 事务定义
try{
2. 业务代码执行
3. 更新消息状态成功
4. 事务提交
}catch(Exception e) {
 2. 事务回滚
 3. 更新消息状态为失败或者增加重试次数(消息重试次数小于最大重试次数，则只增加重试次数，到达最大重试次数则消息变为失败, 失败后需要页面手动重试)
}

业务执行失败，则回滚，更新消息状态为失败。
业务执行成功,更新消息状态成功,则提交事务。
业务执行成功，更新消息状态成功时失败，回滚事务，更新消息状态为失败。

问题分析：
1. 业务执行失败，回滚，更新消息状态为失败时出现问题。
   则该消息可被继续重试消费，因为上次事务已经回滚，因此只是会影响重试次数的正确性，不存在数据不一致问题
2. 业务执行成功,更新消息状态成功,则提交事务。
   如果提交事务时因为数据库等原因出现异常，事务回滚了，消息状态已经更新为成功而更新状态失败没有执行成功，则该消息不再会被消费，此时出现数据不一致问题
3. 业务执行成功，更新消息状态成功时失败。
   更新状态成功失败是因为网络原因请求返回异常或者feign超时等，实际asgard-service已经将该消息状态更新为完成，
   此时更新状态失败没有执行成功，则该消息不再会被消费，此时出现数据不一致问题。

为解决此种情况状态不一致可以将@SagaTask的enabledDbRecord设置为true，可以提高一致性。
```

#### 消费端启用enabledDbRecord提高一致性
```groovy
package db

databaseChangeLog(logicalFilePath:'saga_task_instance_record.groovy') {
    changeSet(id: '2018-08-06-add-table-saga_task_instance_record', author: 'flyleft') {
        createTable(tableName: "saga_task_instance_record") {
            column(name: 'id', type: 'BIGINT UNSIGNED', autoIncrement: false, remarks: '消息id') {
                constraints(primaryKey: true)
            }
            column(name: 'create_time', type: 'BIGINT UNSIGNED', remarks: '创建时间戳')
        }
    }
}
```

```
1. 服务需添加如上表
2. 原理：

1. 记录表插入消息记录
2. 事务定义
try{
3. 业务代码执行
4. 更新消息状态成功
5. 记录表插入消息删除记录
6. 事务提交
}catch(Exception e) {
 3. 事务回滚
 4. 更新消息状态为失败或者增加重试次数
 5. 记录表插入消息删除记录
}

1. 记录表插入消息记录失败则不不再执行。
2. 业务执行成功,更新消息状态成功,则提交事务。
   如果提交事务时因出现异常，则该记录未被删除，进入catch执行回滚，更新消息状态为失败，若更新时失败，则不执行下一步记录删除。存在消息更新失败的记录。
   如果提交事务时因宕机出现异常，未进入catch，数据库事务回滚。存在消息更新状态失败的记录。
3. 业务执行成功，更新消息状态成功时失败。
   更新状态成功失败是因为网络原因请求返回异常或者feign超时等，实际asgard-service已经将该消息状态更新为完成
   更新状态失败没有执行成功。存在消息更新状态失败的记录。
   
如果存在@SagaTask的enabledDbRecord为true，则定时任务会将数据库记录的消息状态更新为失败,直到完成才会再次拉取消息。
```