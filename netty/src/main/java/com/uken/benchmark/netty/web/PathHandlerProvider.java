package com.uken.benchmark.netty.web;

import com.uken.benchmark.netty.annotation.RequestMapping;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class PathHandlerProvider {

  @Autowired private ApplicationContext context;

  private final Logger logger = LoggerFactory.getLogger(PathHandlerProvider.class);

  private Map<String, Function<FullHttpRequest, Object>> getStorage = new HashMap<>();
  private Map<String, Function<FullHttpRequest, Object>> postStorage = new HashMap<>();

  @PostConstruct
  private void init() {

    logger.info("finding controllers...");
    Map<String, Object> beans = context.getBeansWithAnnotation(Controller.class);

    for (Object bean : beans.values()) {
      logger.info("found controller:" + bean.getClass().toString());

      Method[] methods = bean.getClass().getDeclaredMethods();

      for (Method method : methods) {
        logger.info("found method:" + method.getName());
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation != null) {

          String path = annotation.value();

          logger.info("annotation found:" + path);
          Parameter[] parameters = method.getParameters();

          logger.info("parameters:" + parameters.length + " " + parameters[0].getType().toString());

          if (parameters.length == 1
              && parameters[0].getType().isAssignableFrom(FullHttpRequest.class)) {
            logger.info("register path: " + path);
            Map<String, Function<FullHttpRequest, Object>> storage = getStorage;
            if (annotation.method().toUpperCase().equals("POST")) {
              storage = postStorage;
            }
            storage.put(
                path,
                fullHttpRequest -> {
                  Object obj = null;
                  try {
                    obj = method.invoke(bean, fullHttpRequest);
                  } catch (Exception e) {
                    logger.error("error happens", e);
                  }
                  return obj;
                });
          }
        }
      }
    }
  }

  public Function<FullHttpRequest, Object> getHandler(FullHttpRequest request) {

    Map<String, Function<FullHttpRequest, Object>> storage = getStorage;
    if (request.method() == HttpMethod.POST) {
      storage = postStorage;
    }
    for (Entry<String, Function<FullHttpRequest, Object>> entry : storage.entrySet()) {
      if (request.uri().startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    return null;
  }
}
