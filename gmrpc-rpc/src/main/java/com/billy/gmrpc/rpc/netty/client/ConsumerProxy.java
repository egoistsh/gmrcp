package com.billy.gmrpc.rpc.netty.client;

import com.billy.gmrpc.registry.IRegistry;
import com.billy.gmrpc.registry.RegistryFactory;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.rpc.RpcFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ConsumerProxy {
    public static <T> T create(final Class<T> interfaceClass) {
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RequestDTO requestDTO = new RequestDTO();
                        requestDTO.setClassName(method.getDeclaringClass().getName());
                        requestDTO.setMethodName(method.getName());
                        requestDTO.setTypes(method.getParameterTypes());
                        requestDTO.setParams(args);

                        IRegistry registry = RegistryFactory.getRegistry();
                        String service = interfaceClass.getName();
                        String serviceAddress = registry.discover(service);
                        Object result = RpcFactory.getConsumerServer().execute(serviceAddress, requestDTO);
                        return result;
                    }
                }
        );
        return (T) object;
    }
}
