package com.billy.gmrpc.registry;

import com.billy.gmrpc.common.config.Property;
import com.billy.gmrpc.common.extension.ExtensionLoader;

public class RegistryFactory {

    public static IRegistry getRegistry() {
        String protocol = Property.Registry.protocol;
        IRegistry registry = ExtensionLoader.getExtensionLoader(IRegistry.class).getExtension(protocol);
        return registry;
    }
}
