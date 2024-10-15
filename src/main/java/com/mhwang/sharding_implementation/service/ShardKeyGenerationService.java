package com.mhwang.sharding_implementation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static java.lang.Integer.parseInt;

@Service
public class ShardKeyGenerationService {

    private final int SHARD_COUNT = 2;

    @Autowired
    private SequenceNumberService sequenceNumberService;

    public String generateShardKey(int shardNumber, Timestamp timestamp) {
        //shard number already know

        return shardNumber + "-" + sequenceNumberService.getSequenceNumber(timestamp) + "-" + timestamp.getTime();
    }

    public String generateShardKey(int shardNumber) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return generateShardKey(shardNumber, timestamp);
    }

    public String generateShardKey() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        int shardNumber = (int) ((timestamp.getTime() % SHARD_COUNT) + 1);

        return generateShardKey(shardNumber, timestamp);
    }

    public int getShardNumberFromKey(String key) {

        String[] splitKey = key.split("-");

        return parseInt(splitKey[0]);
    }

}
