package com.billy.gmrpc.demo.provider;

import com.billy.gmrpc.demo.api.IPerson;
import com.billy.gmrpc.rpc.annotation.Provider;

@Provider(interfaceClazz = IPerson.class)
public class PersonImpl implements IPerson {
    @Override
    public String getName(String s) {
        System.out.println("start");
        return s + " hahaha";
    }
}
