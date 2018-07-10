package com.uken.benchmark.reactive;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class BenchmarkController {

  public Mono<ServerResponse> postJson(ServerRequest request) {

    Mono<Map<String, Object>> body =
        request.bodyToMono((new ParameterizedTypeReference<Map<String, Object>>() {}));

    return ok().contentType(MediaType.APPLICATION_JSON)
        .body(body, new ParameterizedTypeReference<Map<String, Object>>() {});
  }

  public Mono<ServerResponse> postText(ServerRequest request) {
    return ok().contentType(MediaType.TEXT_PLAIN)
        .body(request.bodyToMono(String.class), String.class);
  }

  public Mono<ServerResponse> getText(ServerRequest request) {
    return ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("hello"), String.class);
  }
}
