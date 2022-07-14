package com.billy.gmrpc.cluster.loadbalance.strategy;

import com.billy.gmrpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> providers) {
        int n = providers.size();
        Random random = new Random();
        int randomNum = random.nextInt(n);
        return providers.get(randomNum);
    }
}
