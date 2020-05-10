~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic peter-topic \
--delete --force

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--topic peter-topic \
--partitions 1 --replication-factor 3 \
--create

~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-producer.sh \
--broker-list localhost:19092,localhost:29092,localhost:39092 \
--topic peter-topic
