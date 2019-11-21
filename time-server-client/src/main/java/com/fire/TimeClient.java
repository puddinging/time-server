package com.fire;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Bootstrap客户端用于简单建立Channel
            //childOption不能用于Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            //NioSocketChannel用于客户端创建Channel
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //指定使用的数据处理方式
                    ch.pipeline().addLast(new TimeClientHandler());
                }

                ;
            });
            //客户端开始连接
            ChannelFuture f = b.connect("localhost", 9090).sync();
            //等待直到这个连接被关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
