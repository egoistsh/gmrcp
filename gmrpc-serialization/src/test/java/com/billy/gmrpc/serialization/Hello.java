package com.billy.gmrpc.serialization;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Hello implements Serializable {
    String name;
    String msg;
}
