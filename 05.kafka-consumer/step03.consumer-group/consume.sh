./create-topic.sh

topic="topic-05-03"
consumerGroup="consumer-group-${topic}"

trap quit SIGINT
function quit() {
    kill -9 `ps -ef | grep ${topic} | awk '{print $2}'`    
	kill -9 `ps -ef | grep ProduceWithPartition | awk '{print $2}'`    
	# rm -rf consume-*.log
}
rm -rf consume-*.log

for((i = 0;i < 3;i++)); do
	~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-consumer.sh \
	--bootstrap-server localhost:19092,localhost:29092,localhost:39092 \
	--topic ${topic} \
	--group ${consumerGroup} > "consume-$((i+1)).log" &
done

sleep 2
cd java-source
./gradlew run > /dev/null &
cd ..

multitail -f $(ls | grep .log)