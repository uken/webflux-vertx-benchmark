package com.uken.benchmark.netty.web;

import static io.netty.buffer.Unpooled.wrappedBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Sharable
public class HttpControllerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private final Logger logger = LoggerFactory.getLogger(HttpControllerHandler.class);

  @Autowired private PathHandlerProvider pathHandlerProvider;

  @Override
  protected void channelRead0(
      ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest)
      throws Exception {

    String responseBody = "";
    HttpResponseStatus responseStatus = HttpResponseStatus.OK;
    AsciiString mediaType = HttpHeaderValues.TEXT_PLAIN;

    Function<FullHttpRequest, Object> handler = pathHandlerProvider.getHandler(fullHttpRequest);

    if (handler == null) {
      return;
    }

    Object response = handler.apply(fullHttpRequest);

    if (response instanceof String) {
      responseBody = String.valueOf(response);
    }

    writeResponse(channelHandlerContext, responseStatus, mediaType, responseBody);
  }

  private void writeResponse(
      ChannelHandlerContext ctx, HttpResponseStatus status, AsciiString mediaType, String body) {
    ByteBuf buf = wrappedBuffer(body.getBytes());
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf);

    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, mediaType.toString() + "; charset=UTF-8");
    HttpUtil.setKeepAlive(response, true);

    ctx.writeAndFlush(response);
  }
}
