package io.quarkus.jokes.deployment.devui;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.FooterPageBuildItem;
import io.quarkus.devui.spi.page.MenuPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.devui.spi.page.WebComponentPageBuilder;
import io.quarkus.jokes.deployment.JokesBuildItem;
import io.quarkus.jokes.runtime.JokesJsonRPCService;

/**
 * This example show how to show some pages in Dev UI
 */
public class JokesDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createJokes(BuildProducer<CardPageBuildItem> cardsProducer,
            BuildProducer<MenuPageBuildItem> menuProducer,
            BuildProducer<FooterPageBuildItem> footerProducer,
            JokesBuildItem jokesBuildItem) {

        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem("Jokes");

        // Add build time data
        cardPageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        // 1) Page(s) that links to external resource.
        cardPageBuildItem.addPage(Page.externalPageBuilder("External HTML")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://randomuser.me/")
                .isHtmlContent());

        cardPageBuildItem.addPage(Page.externalPageBuilder("External Json")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://official-joke-api.appspot.com/jokes/ten")
                .isJsonContent());

        // 2) Page that show build time data in raw json format
        cardPageBuildItem.addPage(Page.rawDataPageBuilder("Raw data")
                .icon("font-awesome-brands:js")
                .label("build-time")
                .buildTimeDataKey("jokes")); // TODO: Auto select the first one

        // 3) Page that show build time data in a table TODO: Add basic format options ?
        cardPageBuildItem.addPage(Page.tableDataPageBuilder("Table data")
                .icon("font-awesome-solid:table")
                .label("build-time")
                .showColumn("timestamp")
                .showColumn("user")
                .showColumn("fullJoke")
                .buildTimeDataKey("jokes")); // TODO: Auto select the first one

        // 4) Page that show build time data that has been formatted using Qute
        cardPageBuildItem.addPage(Page.quteDataPageBuilder("Qute data")
                .icon("font-awesome-solid:q")
                .label("build-time")
                .templateLink("qute-jokes-template.html"));

        // 5) Page create with custom web component
        WebComponentPageBuilder webComponentPage = Page.webComponentPageBuilder()
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-web-components.js")
                .label("build-and run-time");
        cardPageBuildItem.addPage(webComponentPage);
        cardsProducer.produce(cardPageBuildItem);

        // 6) Also add a link in the main menu section
        WebComponentPageBuilder menuItemPageBuilder = Page.webComponentPageBuilder()
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .title("Joke")
                .componentLink("qwc-jokes-menu.js");

        MenuPageBuildItem menuPageBuildItem = new MenuPageBuildItem("Jokes", menuItemPageBuilder);
        menuProducer.produce(menuPageBuildItem);

        // 7) Also add a tab in the footer
        WebComponentPageBuilder jokeLogPageBuilder = Page.webComponentPageBuilder()
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .title("Joke Log")
                .componentLink("qwc-jokes-log.js");

        FooterPageBuildItem footerPageBuildItem = new FooterPageBuildItem("Jokes", jokeLogPageBuilder);
        footerProducer.produce(footerPageBuildItem);

    }

    @BuildStep(onlyIf = IsDevelopment.class)
    JsonRPCProvidersBuildItem createJsonRPCService() {
        return new JsonRPCProvidersBuildItem("Jokes", JokesJsonRPCService.class);
    }
}
