topic="topic-05-02"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-consumer.sh \
--bootstrap-server localhost:19092,localhost:29092,localhost:39092 \
--topic ${topic} \
--partition $1 \
--from-beginning