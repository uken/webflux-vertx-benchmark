package com.uken.benchmark.netty.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HttpServer implements CommandLineRunner {

  @Autowired private HttpChannelInitializer httpChannelInitializer;

  private EpollEventLoopGroup selectGroup = new EpollEventLoopGroup(1);
  private EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(1);

  @Override
  public void run(String... args) throws Exception {
    try {
      ServerBootstrap sb =
          new ServerBootstrap()
              .group(selectGroup, workerGroup)
              .channel(EpollServerSocketChannel.class)
              .childHandler(httpChannelInitializer)
              .option(ChannelOption.SO_BACKLOG, 1024)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);

      ChannelFuture channelFuture = sb.bind(8080);
      channelFuture.sync();
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {

    } finally {
      shutdown();
    }
  }

  @PreDestroy
  void shutdown() {
    selectGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
