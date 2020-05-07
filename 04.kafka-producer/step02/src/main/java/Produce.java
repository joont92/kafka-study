import com.google.gson.Gson;
import model.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Produce {
  public static void main(String[] args) {
    Gson gson = new Gson();

    // 설정
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:19092,localhost:29092,localhost:39092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);

    // produce
    User user = new User(1L, "joont", 29);

    ProducerRecord<String, String> record = new ProducerRecord<>("peter-topic", gson.toJson(user));

    producer.send(record, (m, e) -> {
      if(e == null) {
          System.out.println("kafka event publish response : " + gson.toJson(m));
        } else {
          e.printStackTrace();
        }
      });

    producer.close();
  }
}
