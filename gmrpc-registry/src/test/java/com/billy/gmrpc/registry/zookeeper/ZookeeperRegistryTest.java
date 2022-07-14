package com.billy.gmrpc.registry.zookeeper;

import com.billy.gmrpc.common.extension.ExtensionLoader;
import com.billy.gmrpc.registry.IRegistry;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ZookeeperRegistryTest {

    @Test
    void init() {
    }

    @Test
    void register() throws IOException {
//        IRegistry registry = new ZookeeperRegistry();
        IRegistry registry = ExtensionLoader.getExtensionLoader(IRegistry.class).getExtension("zookeeper");
        registry.register("127.0.0.1:12345", "com.test.Demo");
        System.in.read();
    }

    @Test
    void lookup() {
        IRegistry registry = new ZookeeperRegistry();
        System.out.println(registry.discover("com.test.Demo"));
    }
}