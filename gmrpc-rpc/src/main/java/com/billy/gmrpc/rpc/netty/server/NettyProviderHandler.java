package com.billy.gmrpc.rpc.netty.server;

import com.billy.gmrpc.rpc.Container;
import com.billy.gmrpc.rpc.RequestDTO;
import com.billy.gmrpc.rpc.ResponseDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class NettyProviderHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RequestDTO requestDTO = (RequestDTO) msg;
        Object result = new Object();
        log.info("receive request:{}", requestDTO);
        if (Container.getProviders().containsKey(requestDTO.getClassName())) {
            Object provider = Container.getProviders().get(requestDTO.getClassName());
            Class<?> clazz = provider.getClass();
            Method method = clazz.getMethod(requestDTO.getMethodName(), requestDTO.getTypes());
            result = method.invoke(provider, requestDTO.getParams());
        }

//        ctx.write(result);
        ctx.write(ResponseDTO.success(result));
        ctx.flush();
        ctx.close();
    }

    /**
     * 超时处理
     * 如果超过30秒没收到客户端的心跳，就触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    //todo 不起作用
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            IdleState state = event.state();
            //如果读通道处于空闲状态，说明没有收到心跳命令
            if (IdleState.READER_IDLE.equals(state)) {
                System.out.println("超时");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
