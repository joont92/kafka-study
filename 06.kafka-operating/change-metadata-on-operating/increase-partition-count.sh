./create-topic.sh 1

topic="topic-06-01"
consumerGroup="consumer-group-${topic}"

trap quit SIGINT
function quit() {
    kill -9 `ps -ef | grep ${topic} | awk '{print $2}'`
    kill -9 `ps -ef | grep Produce | awk '{print $2}'`
    # rm -rf consume-*.log
}
rm -rf *.log

for((i = 0;i < 4;i++)); do
	~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-consumer.sh \
	--bootstrap-server localhost:19092,localhost:29092,localhost:39092 \
	--topic ${topic} \
	--group ${consumerGroup} > "consume-$((i+1)).log" &	
done

cd java-source
./gradlew run > ../produce.log &
cd ..

function increasePartition() {
    sleep 10
    ~/workspace/kafka-study/kafka_2.5.0/bin/kafka-topics.sh \
    --zookeeper localhost:2181 \
    # partition 이 늘어나면서 기존의 4개의 컨슈머가 파티션에 각각 리밸런싱 됨
    --alter --topic ${topic} --partitions 4
}

increasePartition &

multitail -f $(ls | grep .log)