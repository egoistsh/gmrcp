package com.billy.gmrpc.rpc.netty.codec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageType {
    REQUEST_PACKAGE(0),
    RESPONSE_PACKAGE(1);

    private final int code;
}
