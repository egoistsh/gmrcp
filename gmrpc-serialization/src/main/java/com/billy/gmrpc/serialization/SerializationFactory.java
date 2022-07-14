package com.billy.gmrpc.serialization;

import com.billy.gmrpc.common.config.Property;
import com.billy.gmrpc.common.extension.ExtensionLoader;

public class SerializationFactory {
    public static ISerialization getSerialization() {
        String serialize = Property.Serialization.serialize;
        ISerialization serialization = ExtensionLoader.getExtensionLoader(ISerialization.class).getExtension(serialize);
        return serialization;
    }
}
