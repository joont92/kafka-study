./create.sh

topic="topic-06-01"
consumerGroup="consumer-group-${topic}"

trap quit SIGINT
function quit() {
    kill -9 `ps -ef | grep ${topic} | awk '{print $2}'`
    rm -rf consume-*.log
}

for((i = 0;i < 4;i++)); do
	~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-consumer.sh \
	--bootstrap-server localhost:19092,localhost:29092,localhost:39092 \
	--topic ${topic} \
	--partition ${i} > "consume-$((i+1)).log" &
done

function increasePartition() {
    sleep 10
    ~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
    --zookeeper localhost:2181 \
    --alter --topic ${topic} --partitions 4
}

increasePartition &

multitail -f $(ls | grep .log)