package com.billy.gmrpc.cluster.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements ILoadBalance {
    @Override
    public String select(List<String> providers) {
        if (providers == null || providers.size() == 0) return null;
        if (providers.size() == 1) return providers.get(0);
        return doSelect(providers);
    }

    protected abstract String doSelect(List<String> providers);

}
