package io.quarkus.jokes.deployment.buildtime;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.deployment.spi.page.Page;
import io.quarkus.devui.deployment.spi.page.PageBuildItem;
import io.quarkus.jokes.deployment.JokesBuildItem;

/**
 * This example show how to show a page in Dev UI using lit and data generated
 */
public class WebComponentsJokesProcessor {

    @BuildStep
    PageBuildItem createJokes(JokesBuildItem jokesBuildItem) {

        PageBuildItem pageBuildItem = new PageBuildItem("Jokes");
        // Add build time data
        pageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        // Page that links to external resource.
        pageBuildItem.addPage(Page.externalPageBuilder("External HTML")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .url("https://randomuser.me/")
                .isHtmlContent()
                .label("external")
                .build());

        // Page that links to external resource.
        pageBuildItem.addPage(Page.externalPageBuilder("External Json")
                .icon("font-awesome-solid:arrow-up-right-from-square")
                .url("https://official-joke-api.appspot.com/jokes/ten")
                .isJsonContent()
                .label("external")
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
