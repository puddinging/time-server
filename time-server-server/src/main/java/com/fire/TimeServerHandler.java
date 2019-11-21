package com.fire;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 实现时间服务器具体逻辑代码
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 每次新的请求来之后，都会进行相应
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf time = ctx.alloc().buffer(4);
//        将时间写入 buffer中
        time.writeInt((int)(System.currentTimeMillis()/1000L+2208988800L));
//        将时间写入输出流，并冲刷出去，由于异步操作，此时并没有真正发送出去
        ChannelFuture f = ctx.writeAndFlush(time);
//        使用future的close回调
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        首先输出堆栈信息，之后关闭ctx
        cause.printStackTrace();
        ctx.close();
    }
}
