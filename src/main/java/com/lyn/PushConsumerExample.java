package com.lyn;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class PushConsumerExample {

    private static final Logger logger = LoggerFactory.getLogger(PushConsumerExample.class);

    public static void main(String[] args) throws ClientException, InterruptedException {
        String endpoint = "192.168.153.128:18082";
        String topic = "TestTopic1";
        ClientServiceProvider provider = ClientServiceProvider.loadService();

        ClientConfiguration configuration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoint)
                .enableSsl(false)
                .build();
        //订阅消息的过滤规则，表示订阅所有tag的消息
        String  tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        //为消费者指定所属的消费者分组，Group需要提前创建
        String consumerGroup = "ConsumerGroup";
        //初始化PushConsumer,需要绑定消费者组、通信参数以及订阅关系
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                //设置消费者分组
                .setConsumerGroup(consumerGroup)
                //设置预绑定的订阅关系
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                //设置消费监听器
                .setMessageListener(messageView -> {
                    //处理消息并返回消费结果
                    logger.info("Consumer message successfully, messageId={}", messageView.getMessageId());
                    return ConsumeResult.SUCCESS;
                })
                .build();
        Thread.sleep(Long.MAX_VALUE);
        //pushConsumer.close();
    }
}
