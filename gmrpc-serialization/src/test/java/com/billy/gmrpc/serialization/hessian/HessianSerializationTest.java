package com.billy.gmrpc.serialization.hessian;

import com.billy.gmrpc.serialization.ISerialization;
import com.billy.gmrpc.serialization.Hello;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HessianSerializationTest {

    @Test
    void serialize() {
        Hello hello = new Hello();
        hello.setName("billy");
        hello.setMsg("hi");
        ISerialization serialization = new HessianSerialization();
        byte[] bytes = serialization.serialize(hello);
        Hello deserialize = serialization.deserialize(bytes, hello.getClass());
        assertEquals(hello.getName(), deserialize.getName());
        assertEquals(hello.getMsg(), deserialize.getMsg());
    }

    @Test
    void deserialize() {
    }
}