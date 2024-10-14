package com.mhwang.sharding_implementation.service;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SequenceNumberService {

    private int count = 0;

    private Timestamp currentTimestamp = null;

    public int getSequenceNumber(Timestamp timestamp) {
        if(currentTimestamp == null || timestamp.after(currentTimestamp)) {
            currentTimestamp = timestamp;
            count = 0;
            return count;
        }

        count += 1;
        return count;
    }
}
