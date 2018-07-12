package com.uken.benchmark.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebFluxApplication {

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
