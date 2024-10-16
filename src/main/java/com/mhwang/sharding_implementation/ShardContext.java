package com.mhwang.sharding_implementation;

import static java.util.Objects.isNull;

public class ShardContext {

    private static final ThreadLocal<Integer> CURRENT_SHARD = new ThreadLocal<>();

    public static int getCurrentShard() {
        if (isNull(CURRENT_SHARD.get())) {
            return 1;
        }
        return CURRENT_SHARD.get();
    }

    public static void setCurrentShard(int shard) {
        CURRENT_SHARD.set(shard);
    }
}
