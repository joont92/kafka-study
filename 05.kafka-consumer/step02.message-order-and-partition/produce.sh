topic="topic-05-02"

for((i = 0;i < 10;i++)); do
	echo $i
    sleep 1
done |
~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-producer.sh \
--broker-list localhost:19092,localhost:29092,localhost:39092 \
--topic ${topic}