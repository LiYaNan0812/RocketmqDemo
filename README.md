## RocketMQ 使用示例
### 1. 安装RocketMQ
[下载源码包](https://dist.apache.org/repos/dist/release/rocketmq/5.2.0/rocketmq-all-5.2.0-source-release.zip "下载地址")

解压并编译
>unzip rocketmq-all-5.2.0-source-release.zip
> cd rocketmq-all-5.2.0-source-release
> mvn -Prelease-all -DskipTests -Dspotbugs.skip=true clean install -U
> cd distribution/target/rokctmq-5.2.0/rocketmq-5.2.0

### 2.启动NameServer
*注意：因启动脚本runserver.sh中默认jvm的最小堆空间为4g，可以修改成512m避免空间不足报错。[参考教程](https://juejin.cn/post/7190568712235057211)*
```shell
$ screen -S nameServer   
$ sh bin/mqnamesrv  
# 显示"The Name Server boot success.."表示启动成功。
```
### 3.启动Broker+Proxy
*注意：因启动脚本runserver.sh中默认jvm的最小堆空间为8g，可以修改成512m避免空间不足报错.[参考教程](https://juejin.cn/post/7190568712235057211)*  
NameServer成功启动后，我们启动Broker和Proxy，5.x 版本下我们建议使用
Local 模式部署，即 Broker 和 Proxy 同进程部署。5.x 版本也支持 Broker 和 
Proxy 分离部署以实现更灵活的集群能力。
```shell
### 先启动broker
$ nohup sh bin/mqbroker -n localhost:9876 --enable-proxy &

### 验证broker是否启动成功, 比如, broker的ip是192.168.1.2 然后名字是broker-a
$ tail -f ~/logs/rocketmqlogs/proxy.log 
The broker[broker-a,192.169.1.2:10911] boot success...
```
### 4.工具测试消息的收发
```shell
$ export NAMESRV_ADDR=localhost:9876
$ sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
$ sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer
```
### 5.程序测试时，创建topic的脚本
>-a +message.type=FIFO 指定topic的消息类型，默认是NORMAL,本示例需要
> 改成FIFO的，避免报错
```shell
$ sh bin/mqadmin updatetopic -n localhost:9876 -t TestTopic -c DefaultCluster -a +message.type=FIFO
```