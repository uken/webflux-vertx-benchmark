package com.uken.benchmark.netty.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uken.benchmark.netty.annotation.RequestMapping;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class BenchmarkController {
  private final Logger logger = LoggerFactory.getLogger(BenchmarkController.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @RequestMapping(value = "/text", method = "POST")
  public String postText(FullHttpRequest request) {
    ByteBuf jsonBuf = request.content();
    String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
    return jsonStr;
  }

  @RequestMapping(value = "/json", method = "POST")
  public Map<String, Object> postJson(FullHttpRequest request) throws Exception {
    ByteBuf jsonBuf = request.content();
    String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);

    Map<String, Object> result =
        objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});

    return result;
  }

  @RequestMapping(value = "/get_text")
  public String getText(FullHttpRequest request) throws Exception {
    return "hello";
  }
}
