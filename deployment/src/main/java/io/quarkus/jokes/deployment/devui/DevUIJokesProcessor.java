package io.quarkus.jokes.deployment.devui;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.deployment.spi.page.Page;
import io.quarkus.devui.deployment.spi.page.PageBuildItem;
import io.quarkus.jokes.deployment.JokesBuildItem;

/**
 * This example show how to show some pages in Dev UI
 */
public class DevUIJokesProcessor {

    @BuildStep
    PageBuildItem createJokes(JokesBuildItem jokesBuildItem) {

        PageBuildItem pageBuildItem = new PageBuildItem("Jokes");

        // Add build time data
        pageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        // 1) Page(s) that links to external resource.
        pageBuildItem.addPage(Page.externalPageBuilder("External HTML")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://randomuser.me/")
                .isHtmlContent()
                .build());

        pageBuildItem.addPage(Page.externalPageBuilder("External Json")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://official-joke-api.appspot.com/jokes/ten")
                .isJsonContent()
                .build());

        // 2) Page that show build time data in raw json format
        pageBuildItem.addPage(Page.rawDataPageBuilder("Raw data")
                .icon("font-awesome-brands:js")
                .label("data")
                .buildTimeDataKey("jokes")
                .build());

        // 3) Page that show build time data in a table
        pageBuildItem.addPage(Page.tableDataPageBuilder("Table data")
                .icon("font-awesome-solid:table")
                .label("data")
                .showColumn("timestamp")
                .showColumn("user")
                .showColumn("fullJoke")
                .buildTimeDataKey("jokes")
                .build());

        // Page create with custom web component
        pageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-web-components.js")
                .label("build-time")
                .build());

        return pageBuildItem;
    }

}
