topic="topic-06-01"

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-reassign-partitions.sh \
--zookeeper localhost:2181 \
--reassignment-json-file rt.json \
--execute

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic ${topic} \
--describe