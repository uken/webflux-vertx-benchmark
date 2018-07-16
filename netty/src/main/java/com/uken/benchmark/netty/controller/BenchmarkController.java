package com.uken.benchmark.netty.controller;

import com.uken.benchmark.netty.annotation.RequestMapping;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class BenchmarkController {
  private final Logger logger = LoggerFactory.getLogger(BenchmarkController.class);

  @RequestMapping(value = "/text", method = "POST")
  public String postText(FullHttpRequest request) {
    ByteBuf jsonBuf = request.content();
    String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
    return jsonStr;
  }
}
