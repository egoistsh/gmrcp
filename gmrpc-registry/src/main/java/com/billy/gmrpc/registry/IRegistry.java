package com.billy.gmrpc.registry;

import com.billy.gmrpc.common.extension.SPI;

@SPI
public interface IRegistry {

    void register(String providerAddress, String service);

    String discover(String service);

}
