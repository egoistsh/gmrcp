package com.billy.gmrpc.cluster.loadbalance.strategy;

import com.billy.gmrpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundLoadBalance extends AbstractLoadBalance {
    private AtomicInteger previous = new AtomicInteger(0);

    @Override
    protected String doSelect(List<String> providers) {
        int n = providers.size();
        if (previous.get() >= n) {
            previous.set(0);
        }
        return providers.get(previous.getAndIncrement());
    }
}
