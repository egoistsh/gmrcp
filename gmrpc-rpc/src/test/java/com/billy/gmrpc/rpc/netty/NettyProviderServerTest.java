package com.billy.gmrpc.rpc.netty;

import com.billy.gmrpc.rpc.IConsumerServer;
import com.billy.gmrpc.rpc.IProviderServer;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.rpc.RpcFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NettyProviderServerTest {

    @Test
    void start() {
    }

    @Test
    void provider() throws IOException {
        IProviderServer providerServer = RpcFactory.getProviderServer();
        providerServer.start("127.0.0.1:23456");
        System.in.read();
    }

    @Test
    void conusmer() {
        IConsumerServer consumerServer = RpcFactory.getConsumerServer();
        Object o = consumerServer.execute("127.0.0.1:23456", new RequestDTO());
        System.out.println(o.toString());
    }
}