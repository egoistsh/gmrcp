package com.billy.gmrpc.common.config;

import com.billy.gmrpc.common.util.PropertyUtils;

public class Property {
    private static final String REGISTRY_PROTOCOL_KEY = "gmrpc.registry.protocol";
    private static final String REGISTRY_ADDRESS_KEY = "gmrpc.registry.address";
    private static final String CLUSTER_LOADBALANCE_KEY = "gmrpc.cluster.loadbalance";
    private static final String RPC_PROTOCOL_KEY = "gmrpc.rpc.protocol";
    private static final String SERIALIZATION_SERIALIZE_KEY = "gmrpc.serialization.serialize";

    public static class Registry {
        public static String protocol = PropertyUtils.getInstance().get(REGISTRY_PROTOCOL_KEY);
        public static String address = PropertyUtils.getInstance().get(REGISTRY_ADDRESS_KEY);
    }

    public static class Cluster {
        public static String loadBalance = PropertyUtils.getInstance().get(CLUSTER_LOADBALANCE_KEY);
    }

    public static class Rpc {
        public static String protocol = PropertyUtils.getInstance().get(RPC_PROTOCOL_KEY);
    }

    public static class Serialization {
        public static String serialize = PropertyUtils.getInstance().get(SERIALIZATION_SERIALIZE_KEY);
    }
}
