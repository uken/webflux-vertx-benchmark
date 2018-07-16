package com.uken.benchmark.netty.web;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

  @Autowired HttpControllerHandler httpControllerHandler;

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast(new HttpRequestDecoder());
    pipeline.addLast(new HttpObjectAggregator(1048576)); // 1MB
    pipeline.addLast(new HttpResponseEncoder());
    pipeline.addLast(httpControllerHandler);
  }
}
