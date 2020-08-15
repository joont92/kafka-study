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
    // 기본값이 5분이라 파티션 사이즈가 늘어나도 5분간은 기존 파티션에 publish 한다
    // % 연산에서 partitionCount 를 계속 기존값(e.g. 1)으로 잡고 있는듯..
    props.put("metadata.max.age.ms", 10); 

    Producer<String, String> producer = new KafkaProducer<>(props);

    // produce
    for (long i = 1; i <= 100000; i++) {
      String key = String.valueOf((int)(Math.random() * 3) + 1);
      producer.send(new ProducerRecord<>(topicName, key, gson.toJson(new User(i, "joont", 29))), 
        (m, e) -> {
          if(e == null) {
              System.out.println("key : " + key + ", response : " + gson.toJson(m));
            } else {
              e.printStackTrace();
            }
        });

      try { Thread.sleep(10); } catch(Exception e) { /*  */ }
    }

    producer.close();
  }
}
