package com.billy.gmrpc.rpc;

import com.billy.gmrpc.common.extension.SPI;

@SPI
public interface IConsumerServer {
    Object execute(String address, RequestDTO requestDTO);
}
