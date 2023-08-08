package io.quarkus.jokes.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class JokesRecorder {

    public Handler<RoutingContext> jokesHandler() {
        return new JokesHandler();
    }
}
