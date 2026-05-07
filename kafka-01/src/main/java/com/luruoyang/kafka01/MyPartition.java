package com.luruoyang.kafka01;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class MyPartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partition = cluster.partitionsForTopic(topic);

        // return ThreadLocalRandom.current().nextInt(partition.size()); // 随机轮询策略
        return Math.abs(key.hashCode()) % partition.size(); // key-ordering策略
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
