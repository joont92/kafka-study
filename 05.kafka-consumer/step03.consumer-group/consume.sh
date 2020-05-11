rm -rf consumer-*.log

for((i = 0;i < 3;i++)); do
	~/workspace/kafka-study/kafka_2.5.0/bin/kafka-console-consumer.sh \
	--bootstrap-server localhost:19092,localhost:29092,localhost:39092 \
	--topic topic-05-04 \
	--group consumer-group-05-03 \
	--from-beginning > "consumer-$((i+1)).log" &
done

multitail -f $(ls | grep .log)