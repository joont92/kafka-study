import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Consume {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    // props.put("group.id", "peter-consumer");
    // props.put("enable.auto.commit", "true");
    // props.put("auto.offset.reset", "latest");
    
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    consumer.subscribe(Arrays.asList("topic-05-01"));

    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("Topic: %s, Partition: %s, Offset: %d, Key: %s, Value: %s\n", 
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
        }
      }
    } finally {
      consumer.close();
    }
  }
}