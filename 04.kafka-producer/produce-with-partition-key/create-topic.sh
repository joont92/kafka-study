topic="topic-04-02"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--delete --force

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--partitions 2 --replication-factor 1 \
--create

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--describe
