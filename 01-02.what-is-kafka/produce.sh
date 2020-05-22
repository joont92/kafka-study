topic="topic-02"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-producer.sh \
--broker-list localhost:19092,localhost:29092,localhost:39092 \
--topic ${topic}
