import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Produce {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);
    ProducerRecord<String, String> producerRecord =
      new ProducerRecord<>("peter-topic", "Apache Kafka is a distributed streaming platform");

    producer.send(producerRecord);
    producer.close();
  }
}
