package com.uken.benchmark;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class App extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(App.class);

  private static final CharSequence RESPONSE_TYPE_JSON =
      HttpHeaders.createOptimized("application/json");
  private static final CharSequence HEADER_CONTENT_TYPE =
      HttpHeaders.createOptimized("content-type");

  @Override
  public void start() {
    final int port = 8080;

    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router
        .post("/json")
        .handler(
            ctx -> {
              HttpServerResponse response = ctx.response();
              JsonObject body = ctx.getBodyAsJson();
              String jsonString = body.toString();
              response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json").end(jsonString);
            });

    router
        .post("/text")
        .handler(
            ctx -> {
              String body = ctx.getBodyAsString();
              ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain").end(body);
            });

    router
        .get("/get_text")
        .handler(
            ctx -> {
              ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain").end("hello");
            });

    vertx.createHttpServer().requestHandler(router::accept).listen(port);
  }
}
