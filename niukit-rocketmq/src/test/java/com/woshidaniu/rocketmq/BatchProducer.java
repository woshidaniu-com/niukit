package com.woshidaniu.rocketmq;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import com.woshidaniu.rocketmq.hooks.MQProducerShutdownHook;

/**
 * http://rocketmq.apache.org/docs/batch-example/
 */
public class BatchProducer {

	public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //Launch the instance.
        producer.start();
        
        String topic = "BatchTest";
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));
        try {
            producer.send(messages);
        } catch (Exception e) {
            e.printStackTrace();
            //handle the error
        }
            
        
        //then you could split the large list into small ones:
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()) {
           try {
               List<Message>  listItem = splitter.next();
               producer.send(listItem);
           } catch (Exception e) {
               e.printStackTrace();
               //handle the error
           }
        }
        
        /**
		 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从RocketMQ服务器上注销自己
		 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
		 */
		Runtime.getRuntime().addShutdownHook(new MQProducerShutdownHook(producer));
		System.exit(0);  
    }
	
}
