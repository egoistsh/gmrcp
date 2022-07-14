package com.billy.gmrpc.serialization;

import com.billy.gmrpc.common.extension.SPI;

@SPI
public interface ISerialization {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
