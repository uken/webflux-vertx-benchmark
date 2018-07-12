# webflux-vertx-benchmark
# Build & Run vert.x 
cd vertx
mvn package
java -jar ./target/vertx-fat.jar

# Build & Run webflux
cd webflux
mvn package
java -jar ./target/webflux-0.0.1-SNAPSHOT.jar


# There will be three endpoints for each service:
(POST)/json    , will echo what ever json from body to the response

(POST)/text    , will echo what ever text from body to the response

(GET)/get_text , will send "hello" text to the response

