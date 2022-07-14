package com.billy.gmrpc.rpc.netty.codec;

import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.serialization.ISerialization;
import com.billy.gmrpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonDecoder extends ByteToMessageDecoder {
    //魔数，标识这是一个协议包
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private Class<?> genericClass;

    public CommonDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magic = byteBuf.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("错误的协议包：{}", magic);
            throw new RuntimeException("错误的协议包");
        }
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACKAGE.getCode()) {
            packageClass = RequestDTO.class;
        }
        int serializerType = byteBuf.readInt();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        ISerialization serialization = SerializationFactory.getSerialization();
        Object object = serialization.deserialize(bytes, genericClass);
        log.info("after decoder: {}", genericClass.cast(object));
        list.add(object);
    }
}
