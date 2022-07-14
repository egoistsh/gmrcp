package com.billy.gmrpc.registry;

import com.billy.gmrpc.cluster.loadbalance.ILoadBalance;
import com.billy.gmrpc.cluster.loadbalance.LoadBalanceFactory;
import com.billy.gmrpc.common.config.Property;

import java.util.List;

public abstract class AbstractRegistry implements IRegistry{
    protected static final String FOLDER = "/gmrpc";
    protected static final String SEPARATOR = "/";

    public AbstractRegistry() {
        String registryAddress = Property.Registry.address;
        init(registryAddress);
    }

    @Override
    public String discover(String service) {
        List<String> providers = lookup(service);
        ILoadBalance loadBalance = LoadBalanceFactory.getLoadBalance();
        return loadBalance.select(providers);
    }

    protected abstract void init(String registryAddress);

    protected abstract List<String> lookup(String service);

}
