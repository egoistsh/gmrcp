package com.billy.gmrpc.rpc.netty.codec;

import com.billy.gmrpc.common.config.Property;
import com.billy.gmrpc.common.extension.ExtensionLoader;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.serialization.ISerialization;
import com.billy.gmrpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Package Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 */
@Slf4j
public class CommonEncoder extends MessageToByteEncoder {
    //魔数，标识这是一个协议包
    private static final int MAGIC_NUMBER = 0xCAFEBABE;


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(MAGIC_NUMBER);
        if (o instanceof RequestDTO) {
            byteBuf.writeInt(PackageType.REQUEST_PACKAGE.getCode());
        } else {
            byteBuf.writeInt(PackageType.RESPONSE_PACKAGE.getCode());
        }
        ISerialization serialization = SerializationFactory.getSerialization();
        //todo 序列化版本号暂时写死，思考如何优雅实现
        byteBuf.writeInt(1);
        byte[] bytes = serialization.serialize(o);
        log.info("after encoder: {}", bytes);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
