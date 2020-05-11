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
    
    for (long i = 0; i < 100; i++) {
      if(i % 10 == 0) {
        producer.send(new ProducerRecord<>("topic-05-04", 0, "10", "10의 배수"));
      } else if(i % 5 == 0) {
        producer.send(new ProducerRecord<>("topic-05-04", 1, "5", "5의 배수"));
      } else {
        producer.send(new ProducerRecord<>("topic-05-04", 2, "0", "나머지"));
      }
    }

    producer.close();
  }
}
