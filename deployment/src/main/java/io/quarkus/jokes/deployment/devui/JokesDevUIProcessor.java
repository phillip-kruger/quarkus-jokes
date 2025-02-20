package io.quarkus.jokes.deployment.devui;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.dev.ai.AIBuildItem;
import io.quarkus.deployment.dev.ai.AIClient;
import io.quarkus.deployment.dev.ai.DynamicOutput;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.buildtime.BuildTimeActionBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.FooterPageBuildItem;
import io.quarkus.devui.spi.page.MenuPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.jokes.deployment.JokesBuildItem;
import io.quarkus.jokes.runtime.JokesJsonRPCService;
import io.quarkus.jokes.runtime.JokesRecorder;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * This example show how to show some pages in Dev UI
 */
public class JokesDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createJokes(BuildProducer<CardPageBuildItem> cardsProducer,
            BuildProducer<MenuPageBuildItem> menuProducer,
            BuildProducer<FooterPageBuildItem> footerProducer,
            JokesBuildItem jokesBuildItem,
            AIBuildItem aIBuildItem) {

        // Cards

        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();

        cardPageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        //cardPageBuildItem.addPage(Page.externalPageBuilder("Dynamic Link")
        //        .icon("font-awesome-brands:js")
        //        .dynamicUrlJsonRPCMethodName("getDynamicLink"));

        //cardPageBuildItem.addPage(Page.externalPageBuilder("Dynamic Link external")
        //        .icon("font-awesome-brands:js")
        //        .doNotEmbed()
        //        .dynamicUrlJsonRPCMethodName("getDynamicLink"));

        cardPageBuildItem.addPage(Page.rawDataPageBuilder("Raw data")
                .icon("font-awesome-brands:js")
                .buildTimeDataKey("jokes"));

        cardPageBuildItem.addPage(Page.tableDataPageBuilder("Table data")
                .icon("font-awesome-solid:table")
                .showColumn("timestamp")
                .showColumn("user")
                .showColumn("fullJoke")
                .buildTimeDataKey("jokes"));

        cardPageBuildItem.addPage(Page.quteDataPageBuilder("Qute data")
                .icon("font-awesome-solid:q")
                .templateLink("qute-jokes-template.html"));

        cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-brands:vaadin")
                .componentLink("qwc-jokes-vaadin.js"));

        cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-solid:chart-pie")
                .componentLink("qwc-jokes-chart.js"));

        cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-web-components.js"));

        cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-solid:cookie-bite")
                .componentLink("qwc-jokes-cookies.js"));

        try {

            cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                    .title(getFunnyWord(aIBuildItem))
                    .icon("font-awesome-solid:face-grin-squint-tears")
                    .dynamicLabelJsonRPCMethodName("funnyWord")
                    .componentLink("qwc-jokes-ai.js"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        cardsProducer.produce(cardPageBuildItem);

        // Menu

        MenuPageBuildItem menuPageBuildItem = new MenuPageBuildItem();

        menuPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .title("One Joke")
                .componentLink("qwc-jokes-menu.js"));

        menuPageBuildItem.addPage(Page.webComponentPageBuilder()
                .title("Jokes")
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-web-components.js"));

        menuProducer.produce(menuPageBuildItem);

        // Footer

        FooterPageBuildItem footerPageBuildItem = new FooterPageBuildItem();

        footerPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .title("Joke Log")
                .componentLink("qwc-jokes-log.js"));

        footerProducer.produce(footerPageBuildItem);

    }

    @BuildStep(onlyIf = IsDevelopment.class)
    JsonRPCProvidersBuildItem createJsonRPCService() {
        return new JsonRPCProvidersBuildItem(JokesJsonRPCService.class);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerSomeEndpoint(
            BuildProducer<RouteBuildItem> routeProducer,
            JokesRecorder recorder,
            NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem) {

        Handler<RoutingContext> handler = recorder.jokesHandler();
        routeProducer.produce(nonApplicationRootPathBuildItem.routeBuilder()
                .route("jokes-endpoint")
                .handler(handler)
                .build());
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    BuildTimeActionBuildItem createBuildTimeActions(AIBuildItem aIBuildItem) {
        BuildTimeActionBuildItem buildTimeActionBuildItem = new BuildTimeActionBuildItem();

        buildTimeActionBuildItem.addAction("funnyWord", ignored -> {
            return getFunnyWord(aIBuildItem);
        });

        buildTimeActionBuildItem.addAction("getAIJoke", params -> {
            AIClient aiClient = aIBuildItem.getAIClient();
            return aiClient.dynamic("Tell a funny joke about why {{something}} is better that {{anotherthing}}", params);
        });

        return buildTimeActionBuildItem;
    }

    private String getFunnyWord(AIBuildItem aIBuildItem) {
        try {
            CompletableFuture<DynamicOutput> dynamic = aIBuildItem.getAIClient()
                    .dynamic("Provide a funny sounding word. Just the word, nothing else", Map.of());
            DynamicOutput dynamicOutput = dynamic.get(15, TimeUnit.SECONDS);
            return dynamicOutput.jsonResponse();
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            return "googoogaga";
        }
    }

}
