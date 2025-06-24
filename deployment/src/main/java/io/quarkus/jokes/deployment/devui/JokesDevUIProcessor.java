package io.quarkus.jokes.deployment.devui;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import io.quarkus.assistant.runtime.dev.Assistant;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.IsLocalDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.buildtime.BuildTimeActionBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.FooterPageBuildItem;
import io.quarkus.devui.spi.page.MenuPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.devui.spi.workspace.Action;
import io.quarkus.devui.spi.workspace.Display;
import io.quarkus.devui.spi.workspace.DisplayType;
import io.quarkus.devui.spi.workspace.Patterns;
import io.quarkus.devui.spi.workspace.WorkspaceActionBuildItem;
import io.quarkus.jokes.deployment.JokesBuildItem;
import io.quarkus.jokes.runtime.JokesJsonRPCService;

/**
 * This example show how to show some pages in Dev UI
 */
public class JokesDevUIProcessor {

    @BuildStep(onlyIf = IsLocalDevelopment.class)
    WorkspaceActionBuildItem createWorkspaceActions() {
        return new WorkspaceActionBuildItem(
                Action.actionBuilder()
                        .label("Joke Lol")
                        .function((t) -> {
                            Map<String, String> input = (Map) t;
                            input.put("content", input.get("content").replace("o", "😂"));
                            input.put("path", input.get("path").replace(".java", ".joke"));
                            return t;
                        })
                        .pathConverter((t) -> {
                            Path original = (Path) t;
                            return original.resolveSibling(original.getFileName().toString().replaceFirst("\\.java$", ".joke"));
                        })
                        .display(Display.dialog)
                        .displayType(DisplayType.code),

                Action.actionBuilder()
                        .label("Joke MD")
                        .function((t) -> {
                            Map<String, String> input = (Map) t;
                            input.put("content", "# Here is your code in markdown \n\n "
                                    + "```java \n"
                                    + input.get("content")
                                    + "```");
                            return t;
                        })
                        .display(Display.split)
                        .displayType(DisplayType.markdown)
                        .filter(Patterns.JAVA_SRC),

                Action.actionBuilder()
                        .label("Joke Reverse")
                        .function((t) -> {
                            Map<String, String> input = (Map) t;
                            input.put("content", new StringBuilder(input.get("content")).reverse().toString());
                            return t;
                        })
                        .display(Display.replace)
                        .displayType(DisplayType.code)
                        .filter(Patterns.JAVA_SRC),

                Action.actionBuilder()
                        .label("Knock-knock")
                        .function((t) -> {
                            return "Who's there ?";
                        })
                        .display(Display.notification)
                        .displayType(DisplayType.raw)
                        .filter(Patterns.JAVA_SRC),

                Action.actionBuilder()
                        .label("Joke AI")
                        .assistantFunction((a, p) -> {
                            Assistant assistant = (Assistant) a;
                            Map params = (Map) p;

                            return assistant.assistBuilder()
                                    .systemMessage(JOKE_SYSTEM_MESSAGE)
                                    .userMessage(JOKE_USER_MESSAGE)
                                    .variables(params)
                                    .assist();

                        })
                        .display(Display.split)
                        .displayType(DisplayType.markdown)
                        .filter(Patterns.JAVA_SRC));

    }

    @BuildStep(onlyIf = IsLocalDevelopment.class)
    void createJokesPageOnCard(BuildProducer<CardPageBuildItem> cardsProducer,
            JokesBuildItem jokesBuildItem) {

        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();
        cardPageBuildItem.setLogo("clown.svg", "clown.svg");
        cardPageBuildItem.addBuildTimeData("jokes", jokesBuildItem.getJokes());

        cardPageBuildItem.addPage(Page.webComponentPageBuilder()
                .title("Joke List")
                .icon("font-awesome-solid:cubes")
                .componentLink("qwc-jokes-list.js"));

        cardPageBuildItem.addPage(Page.rawDataPageBuilder("Raw data")
                .icon("font-awesome-brands:js")
                .buildTimeDataKey("jokes"));

        cardPageBuildItem.addPage(Page.tableDataPageBuilder("Table data")
                .icon("font-awesome-solid:table")
                .showColumn("timestamp")
                .showColumn("user")
                .showColumn("fullJoke")
                .buildTimeDataKey("jokes"));

        cardPageBuildItem.addPage(Page.assistantPageBuilder()
                .title("AI Joke")
                .componentLink("qwc-jokes-ai.js"));

        cardsProducer.produce(cardPageBuildItem);
    }

    @BuildStep(onlyIf = IsLocalDevelopment.class)
    void createJokesMenu(BuildProducer<MenuPageBuildItem> menuProducer) {

        MenuPageBuildItem menuPageBuildItem = new MenuPageBuildItem();

        menuPageBuildItem.addPage(Page.webComponentPageBuilder()
                .title("One Joke")
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .componentLink("qwc-jokes-menu.js"));

        menuProducer.produce(menuPageBuildItem);
    }

    @BuildStep(onlyIf = IsLocalDevelopment.class)
    void createJokesLog(BuildProducer<FooterPageBuildItem> footerProducer) {

        FooterPageBuildItem footerPageBuildItem = new FooterPageBuildItem();

        footerPageBuildItem.addPage(Page.webComponentPageBuilder()
                .icon("font-awesome-regular:face-grin-tongue-wink")
                .title("Joke Log")
                .componentLink("qwc-jokes-log.js"));

        footerProducer.produce(footerPageBuildItem);
    }

    @BuildStep
    void createBuildTimeActions(BuildProducer<BuildTimeActionBuildItem> buildTimeActionProducer) {
        BuildTimeActionBuildItem bta = new BuildTimeActionBuildItem();

        bta.addAssistantAction("getAIJokeInDeployment", (a, p) -> {
            Assistant assistant = (Assistant) a;

            return assistant.assistBuilder()
                    .userMessage(USER_MESSAGE)
                    .variables(p)
                    .assist();
        });
        buildTimeActionProducer.produce(bta);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    JsonRPCProvidersBuildItem createJsonRPCService() {
        return new JsonRPCProvidersBuildItem(JokesJsonRPCService.class);
    }

    private Path getPath(Map params) {
        if (params.containsKey("path")) {
            String filePath = (String) params.get("path");
            URI uri = URI.create(filePath);
            return Paths.get(uri);
        }
        return null;
    }

    private static final String JOKE_SYSTEM_MESSAGE = """
            You will receive content that needs to be used in a joke that you need to tell . You will receive the path and the content, consider that when creating a response.

            Approach this task step-by-step, take your time and do not skip steps.

            Respond with a joke in markdown format, but make sure this markdown in encoded such that it can be added to a json file. This response must be valid markdown. Only include the markdown content, no explanation or other text.

            You must not wrap markdown content in backticks, or in any other way, but return it as plain markdown encoded for json. If the joke contains code, make sure to use the markdown format to display the code properly.

            The markdown content must be returned per path (the path will be provided in the provided content).
             """;

    private static final String JOKE_USER_MESSAGE = """
            Please make a funny joke with the provided content.

            Here are the content:
            {{content}}

            """;

    private static final String USER_MESSAGE = """
            Make a joke about {{something}} is better than {{anotherthing}}
            """;
}
