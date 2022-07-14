package com.billy.gmrpc.rpc;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = -6640045946810153371L;

    private String className;
    private String methodName;
    private Class[] types;
    private Object[] params;
    private String requestId;

}
