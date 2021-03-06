import com.google.gson.Gson;
import model.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProduceWithKey {
  public static void main(String[] args) {
    Gson gson = new Gson();

    // 설정
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);

    // produce
    for (long i = 1; i < 11; i++) {
      if(i % 2 == 0) {
        producer.send(new ProducerRecord<>("peter-topic", "1", gson.toJson(new User(i, "joont", 29))));
      } else {
        producer.send(new ProducerRecord<>("peter-topic", "2", gson.toJson(new User(i, "joont", 29))));
      }
    }

    producer.close();
  }
}
