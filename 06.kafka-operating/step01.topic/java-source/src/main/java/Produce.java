import com.google.gson.Gson;
import model.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Produce {
  private static final Gson gson = new Gson();

  public static void main(String[] args) {
    String topicName = "topic-06-01";

    // 설정
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);

    // produce
    for (long i = 1; i <= 100000; i++) {
      producer.send(new ProducerRecord<>(topicName, 
        String.valueOf((int)(Math.random() * 4) + 1), gson.toJson(new User(i, "joont", 29))));

      try { Thread.sleep(10); } catch(Exception e) { /*  */ }
    }

    producer.close();
  }
}
