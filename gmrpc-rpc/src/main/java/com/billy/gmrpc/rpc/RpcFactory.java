package com.billy.gmrpc.rpc;

import com.billy.gmrpc.common.config.Property;
import com.billy.gmrpc.common.extension.ExtensionLoader;

public class RpcFactory {
    public static IProviderServer getProviderServer() {
        String protocol = Property.Rpc.protocol;
        IProviderServer providerServer = ExtensionLoader.getExtensionLoader(IProviderServer.class).getExtension(protocol);
        return providerServer;
    }

    public static IConsumerServer getConsumerServer() {
        String protocol = Property.Rpc.protocol;
        IConsumerServer consumerServer = ExtensionLoader.getExtensionLoader(IConsumerServer.class).getExtension(protocol);
        return consumerServer;
    }
}
