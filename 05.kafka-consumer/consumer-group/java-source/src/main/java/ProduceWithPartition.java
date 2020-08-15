import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProduceWithPartition {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);
  
    String topic = "topic-05-03";

    for (long i = 0; i < 1000; i++) {
      if(i % 10 == 0) {
        producer.send(new ProducerRecord<>(topic, 0, "0", "10의 배수"));
      } else if(i % 5 == 0) {
        producer.send(new ProducerRecord<>(topic, 1, "1", "5의 배수"));
      } else {
        producer.send(new ProducerRecord<>(topic, 2, "2", "나머지"));
      }
      try { Thread.sleep(100); } catch(Exception e) { /*  */ }
    }

    producer.close();
  }
}
