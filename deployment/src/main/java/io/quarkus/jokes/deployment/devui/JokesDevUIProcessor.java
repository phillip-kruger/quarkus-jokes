package io.quarkus.jokes.deployment.devui;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.deployment.spi.page.Page;
import io.quarkus.devui.deployment.spi.page.PageBuildItem;
import io.quarkus.devui.deployment.spi.runtime.JsonRPCProvidersBuildItem;
import io.quarkus.jokes.deployment.JokesBuildItem;
import io.quarkus.jokes.runtime.JokesJsonRPCService;

/**
 * This example show how to show some pages in Dev UI
 */
public class JokesDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    PageBuildItem createJokes(JokesBuildItem jokesBuildItem) {

        PageBuildItem pageBuildItem = new PageBuildItem("Jokes");

        // Add build time data
        pageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        // 1) Page(s) that links to external resource.
        pageBuildItem.addPage(Page.externalPageBuilder("External HTML")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://randomuser.me/")
                .isHtmlContent());

        pageBuildItem.addPage(Page.externalPageBuilder("External Json")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .label("external")
                .url("https://official-joke-api.appspot.com/jokes/ten")
                .isJsonContent());

        // 2) Page that show build time data in raw json format
        pageBuildItem.addPage(Page.rawDataPageBuilder("Raw data")
                .icon("font-awesome-brands:js")
                .label("build-time")
                .buildTimeDataKey("jokes")); // TODO: Auto select the first one

        // 3) Page that show build time data in a table TODO: Add basic format options ?
        pageBuildItem.addPage(Page.tableDataPageBuilder("Table data")
                .icon("font-awesome-solid:table")
                .label("build-time")
                .showColumn("timestamp")
                .showColumn("user")
                .showColumn("fullJoke")
                .buildTimeDataKey("jokes")); // TODO: Auto select the first one

        // 4) Page that show build time data that has been formatted using Qute
        pageBuildItem.addPage(Page.quteDataPageBuilder("Qute data")
                .icon("font-awesome-solid:q")
                .label("build-time")
                .templateLink("qute-jokes-template.html"));

        // 5) Page create with custom web component
        pageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-web-components.js")
                .label("build-and run-time"));

        return pageBuildItem;
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    JsonRPCProvidersBuildItem createJsonRPCService() {
        return new JsonRPCProvidersBuildItem("Jokes", JokesJsonRPCService.class);
    }
}
