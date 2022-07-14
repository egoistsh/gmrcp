package com.billy.gmrpc.demo.provider;

import com.billy.gmrpc.demo.api.IHello;
import com.billy.gmrpc.rpc.annotation.Provider;

@Provider(interfaceClazz = IHello.class)
public class HelloImpl implements IHello {
    @Override
    public String hello(String message) {
        return "hello " + message;
    }
}
