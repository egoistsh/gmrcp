package com.billy.gmrpc.rpc.netty.client;

import com.billy.gmrpc.rpc.IConsumerServer;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.rpc.ResponseDTO;
import com.billy.gmrpc.rpc.netty.client.ConsumerProxy;
import com.billy.gmrpc.rpc.netty.client.NettyConsumerHandler;
import com.billy.gmrpc.rpc.netty.codec.CommonDecoder;
import com.billy.gmrpc.rpc.netty.codec.CommonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyConsumerServer implements IConsumerServer {
    @Override
    public Object execute(String address, RequestDTO requestDTO) {
        String[] addrs = address.split(":");
        String host = addrs[0];
        Integer port = Integer.valueOf(addrs[1]);
        final NettyConsumerHandler consumerHandler = new NettyConsumerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast( new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(ConsumerProxy.class.getClassLoader())));
//                            pipeline.addLast( new ObjectEncoder());
                            //如果5秒没消息发送到服务端，就发送心跳。
                            pipeline.addLast(new IdleStateHandler(0, 2, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new CommonDecoder(ResponseDTO.class));
                            pipeline.addLast(new CommonEncoder());
                            pipeline.addLast(consumerHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(requestDTO);
            log.info("send request..., {}", requestDTO);
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
        return consumerHandler.getResponse();
    }
}
