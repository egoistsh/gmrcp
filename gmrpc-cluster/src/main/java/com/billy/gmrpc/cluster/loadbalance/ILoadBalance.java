package com.billy.gmrpc.cluster.loadbalance;

import com.billy.gmrpc.common.extension.SPI;

import java.util.List;

@SPI
public interface ILoadBalance {
    String select(List<String> providers);
}
