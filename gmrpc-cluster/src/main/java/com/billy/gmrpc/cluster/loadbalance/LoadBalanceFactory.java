package com.billy.gmrpc.cluster.loadbalance;

import com.billy.gmrpc.cluster.loadbalance.strategy.RandomLoadBalance;
import com.billy.gmrpc.common.config.Property;
import com.billy.gmrpc.common.extension.ExtensionLoader;

public class LoadBalanceFactory {

    public static ILoadBalance getLoadBalance() {
        String loadBalance = Property.Cluster.loadBalance;
        //SPI
        ILoadBalance extension = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getExtension(loadBalance);
        return extension;
    }
}
