package com.billy.gmrpc.rpc;

import com.billy.gmrpc.common.extension.SPI;

@SPI
public interface IProviderServer {
    void start(String selfAddress);
}
