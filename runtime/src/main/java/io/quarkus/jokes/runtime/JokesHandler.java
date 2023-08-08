package io.quarkus.jokes.runtime;

import java.util.Set;

import io.quarkus.arc.Arc;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;

public class JokesHandler implements Handler<RoutingContext> {

    private JokesJsonRPCService jokesJsonRPCService;

    @Override
    public void handle(RoutingContext event) {
        MultiMap params = event.request().params();
        String location = "/q/dev-ui"; // default
        if (params.contains("callback")) {
            location = params.get("callback");
        }

        Set<Cookie> cookies = event.request().cookies();
        getJokesJsonRPCService().setCookies(cookies);

        event.response().setStatusCode(303).putHeader("Location", location).end();
    }

    private JokesJsonRPCService getJokesJsonRPCService() {
        if (this.jokesJsonRPCService == null) {
            this.jokesJsonRPCService = Arc.container().instance(JokesJsonRPCService.class).get();
        }
        return this.jokesJsonRPCService;
    }

}
