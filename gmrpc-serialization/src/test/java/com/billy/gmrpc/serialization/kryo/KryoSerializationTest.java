package com.billy.gmrpc.serialization.kryo;

import com.billy.gmrpc.serialization.Hello;
import com.billy.gmrpc.serialization.ISerialization;
import com.billy.gmrpc.serialization.SerializationFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KryoSerializationTest {

    @Test
    void serialize() {
        Hello hello = new Hello();
        hello.setName("billy");
        hello.setMsg("hi");
        ISerialization serialization = new KryoSerialization();
        byte[] bytes = serialization.serialize(hello);
        Hello deserialize = serialization.deserialize(bytes, hello.getClass());
        assertEquals(hello.getName(), deserialize.getName());
        assertEquals(hello.getMsg(), deserialize.getMsg());
    }

    @Test
    void serializeSPI() {
        Hello hello = new Hello();
        hello.setName("billy");
        hello.setMsg("hi");
        ISerialization serialization = SerializationFactory.getSerialization();
        byte[] bytes = serialization.serialize(hello);
        Hello deserialize = serialization.deserialize(bytes, hello.getClass());
        assertEquals(hello.getName(), deserialize.getName());
        assertEquals(hello.getMsg(), deserialize.getMsg());
    }
}