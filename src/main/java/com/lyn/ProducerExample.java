package com.lyn;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ProducerExample {

    private static final Logger logger = LoggerFactory.getLogger(ProducerExample.class);

    public static void main(String[] args) throws ClientException, IOException {
        //连接点
        String endpoint = "192.168.153.128:18082";
        String topic = "TestTopic1";

        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration configuration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoint)
                .enableSsl(false)
                .build();

        //使用ClientServiceProvider创建生产者，绑定主题，并设置客户端配置
        Producer producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();

        //创建要发送的消息对象Message
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                .setKeys("fisrtKey")
                .setTag("firstTag")
                .setMessageGroup("myGroup")
                .setBody("HA HA HA".getBytes(StandardCharsets.UTF_8))
                .build();
        try {
            //使用生产者发送消息
            SendReceipt send = producer.send(message);
            logger.info("success, messageId is {}", send.getMessageId());
        } catch (ClientException e ){
            logger.error("send fail", e);
        }
        producer.close();
    }
}
