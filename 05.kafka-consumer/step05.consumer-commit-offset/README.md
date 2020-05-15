# 커밋과 오프셋
앞서 언급했듯이 컨슈머는 poll() 을 호출할 때 마다 아직 읽지 않은 메시지를 들고오는데, 이는 컨슈머가 자신이 들고간 메시지의 위치를 직접 기록해 놓기 때문이다  
근데 단순히 컨슈머만 오프셋을 알고 있으면 안되는게,  
컨슈머가 장애로 인해 다운되거나 새로 추가되어 컨슈머 리밸런싱이 일어났을 때, 리밸런싱 된 컨슈머는 기존의 파티션과 다른 파티션과 연결되게 될 것이기 때문이다  
기존과 다른 파티션에 연결된 컨슈머는 해당 파티션에서 다음으로 읽어갈 메시지의 오프셋을 알아야하는데, 리밸런싱 된 상태라 그 정보가 없기 때문에  
이를 따로 관리해주는 저장소가 필요하게 된다  

올드 카프카 컨슈머(0.9 이전)는 이 정보를 주키퍼에 저장했으나, 성능 등의 문제로 뉴 카프카 컨슈머에서는 카프카 내부에 오프셋용 토픽(__consumer_offset)을 만들고 여기에 오프셋 정보를 저장하고 있다  

이곳에 커밋된 오프셋이 실제로 읽어야 할 오프셋보다 작으면 메시지는 중복으로 처리되고,  
커밋된 오프셋이 실제로 읽어야 할 오프셋보다 크면 메시지는 누락되게 된다  
이렇듯 카프카의 오프셋을 커밋하는 것은 매우 중요하므로, 카프카에서 제공해주는 커밋 옵션을 잘 살펴보고 나에게 맞는 전략을 잘 선택해야 한다  

### 자동 커밋
컨슈머 옵션의 `enable.auto.commit`을 `true`로 설정해주면 5초 주기로 자신이 읽어온 가장 마지막 오프셋을 카프카에 커밋한다  
(주기는 `auto.commit.interval.ms` 옵션으로 조정 가능하다)  

근데 이러한 자동 커밋의 경우, 아래와 같은 문제가 발생할 수 있다  
1. 자동 커밋 주기가 5초인 상태이다
2. 컨슈머가 브로커로부터 메시지를 읽어온 후 3초가 지난 상태이다
3. 이 때 컨슈머에 장애가 발생하여 컨슈머에 리밸런싱이 일어났다
4. 리밸런싱된 컨슈머는 기존의 파티션에서 메시지를 가져와야하는데, 이전 컨슈머가 자신이 읽은 메시지의 오프셋을 커밋하지 않았으므로, 새로운 컨슈머는 이전에 읽었던 메시지를 또 읽게 된다

커밋 주기를 줄여서 이러한 문제점의 발생 가능성을 줄일 순 있지만, 100% 제거하는 것은 불가능하다  
자동 커밋의 경우 편리한 대신 이런 불편함이 있으니, 잘 생각하고 선택해야 한다

### 수동 커밋
말 그대로 커밋을 직접 호출하여 오프셋을 기록하는 방식이다  

```java
public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "peter-kafka001:9092,peter-kafka002:9092,peter-kafka003:9092");
    props.put("group.id", "peter-manual");
    props.put("enable.auto.commit", "false"); // auto commit false
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("peter-topic"));
    
    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        for (ConsumerRecord<String, String> record : records) {
            insertIntoDB(record); // DB에 값 저장
        try {
            consumer.commitSync(); // 직접 커밋
        } catch (CommitFailedException e) {
            System.out.printf("commit failed", e);
        }
    }
}
```

위처럼 오프셋을 기록하기 전에 통과해야하는 전처리가 있을 경우 사용하기 좋다  

- 일단, consumer 에 커밋하는 부분은 db 트랜잭션과 같이 묶지 않는것이 좋다
    - 네트워크 통신하는 부분을 트랜잭션 안에 넣는것은 낭비가 되고, 위험할 수 있다
- 위처럼 전처리에서 예외가 발생하지 않아야 오프셋을 커밋하는 구조라면, 
- 그렇다고 오프셋을 먼저 커밋하고 후처리 형태로 수행하게 될 경우, 후처리에서 예외 발생 시 

하지만 전처리에서 에러가 발생할 경우 계속해서 다음 메시지로 넘어가지 못하고 중복된 메시지를 읽어오게 되는것에 주의해야 한다  
트랜잭션으로 같이 묶어도 되지만, 네트워크 비용 발생

### 특정 오프셋부터 메시지 가져오기
파티션, 오프셋을 지정하여 메시지를 직접 가져올 수도 있다  

```java
TopicPartition partition0 = new TopicPartition(topic, 0);
TopicPartition partition1 = new TopicPartition(topic, 1);

consumer.assign(Arrays.asList(partition0, partition1));
consumer.seek(partition0, offsetNumber);
consumer.seek(partition1, offsetNumber);
```

`seek()` 메서드를 통해 오프셋을 지정해 메시지를 직접 읽어올 수 있다  
하지만 오프셋의 경우 직접 관리하면 매우 번거로워 지므로 자동으로 맡기는 것이 가장 좋다  