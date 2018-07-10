package com.uken.benchmark.reactive;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MyRoutes {

  @Bean
  public RouterFunction<ServerResponse> routes(BenchmarkController benchmarkController) {
    return route(POST("/json"), benchmarkController::postJson)
        .andRoute(POST("/text"), benchmarkController::postText)
        .andRoute(GET("/get_text"), benchmarkController::getText);
  }
}
