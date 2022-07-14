package com.billy.gmrpc.rpc.netty.server;

import com.billy.gmrpc.rpc.Container;
import com.billy.gmrpc.rpc.IProviderServer;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.rpc.netty.codec.CommonDecoder;
import com.billy.gmrpc.rpc.netty.codec.CommonEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyProviderServer implements IProviderServer {
    @Override
    public void start(String selfAddress) {
        Container.registerSelf(selfAddress);

        String[] addrs = selfAddress.split(":");
        String ip = addrs[0];
        Integer port = Integer.valueOf(addrs[1]);
        publish(ip, port);
    }

    private void publish(String ip, Integer port) {
        //启动服务
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new ObjectEncoder());
//                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            // 30秒没收到客户端请求的话就关闭连接
                            pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            //自定义encoder、decoder，在其中进行序列化和反序列
                            pipeline.addLast(new CommonEncoder());
                            pipeline.addLast(new CommonDecoder(RequestDTO.class));
                            pipeline.addLast(new NettyProviderHandler());
                        }
                    })
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 是否开启 TCP 底层心跳检测
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("netty server is started...");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
