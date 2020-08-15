./create.sh 1

topic="topic-06-01"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-configs.sh \
--zookeeper localhost:2181 \
--alter --entity-type topics --entity-name ${topic} \
--add-config retention.ms=3600000

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--describe

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-configs.sh \
--zookeeper localhost:2181 \
--alter --entity-type topics --entity-name ${topic} \
--delete-config retention.ms

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--describe