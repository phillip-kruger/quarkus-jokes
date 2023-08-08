package io.quarkus.jokes.deployment.devui;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
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
            JokesBuildItem jokesBuildItem) {

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
}
