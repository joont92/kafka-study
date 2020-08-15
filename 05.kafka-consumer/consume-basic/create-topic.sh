topic="topic-05-01"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--delete --force

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--partitions 1 --replication-factor 3 \
--create

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--describe
