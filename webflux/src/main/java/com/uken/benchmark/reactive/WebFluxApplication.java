package com.uken.benchmark.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@SpringBootApplication
public class WebFluxApplication {

  @Bean
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory) {
    return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
  }

  //  @Bean
  //  public ReactiveWebServerFactory reactiveWebServerFactory() {
  //    NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
  //    factory.addServerCustomizers(
  //        builder -> builder.loopResources(LoopResources.create("my-http", 1, 1, true)));
  //
  //    return factory;
  //  }

  public static void main(String[] args) {
    SpringApplication.run(WebFluxApplication.class, args);
  }
}
