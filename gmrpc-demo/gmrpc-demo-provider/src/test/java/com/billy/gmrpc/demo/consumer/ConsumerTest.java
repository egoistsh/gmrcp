package com.billy.gmrpc.demo.consumer;

import com.billy.gmrpc.demo.api.IHello;
import com.billy.gmrpc.demo.api.IPerson;
import com.billy.gmrpc.rpc.IProviderServer;
import com.billy.gmrpc.rpc.RpcFactory;
import com.billy.gmrpc.rpc.netty.client.ConsumerProxy;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConsumerTest {
    @Test
    void provider() throws IOException {
        IProviderServer providerServer = RpcFactory.getProviderServer();
        providerServer.start("127.0.0.1:28849");
        System.in.read();
    }

    @Test
    void consumer() throws IOException, InterruptedException {
//        IHello hello = ConsumerProxy.create(IHello.class);
        IPerson person = ConsumerProxy.create(IPerson.class);
        String s = person.getName("billy");
        System.out.println(s);
//        Thread.sleep(10000);
        System.in.read();
    }
}
