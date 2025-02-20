package io.quarkus.jokes.deployment.ai;

import java.nio.file.Path;
import java.util.Map;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.BuildSteps;
import io.quarkus.deployment.dev.ai.AIConsoleBuildItem;
import io.quarkus.deployment.dev.ai.workspace.Patterns;
import io.quarkus.deployment.dev.ai.workspace.WorkspaceCreateBuildItem;
import io.quarkus.deployment.dev.ai.workspace.WorkspaceReadBuildItem;
import io.quarkus.deployment.dev.ai.workspace.WorkspaceUpdateBuildItem;

/**
 * This will use AI to tell jokes
 */
@BuildSteps(onlyIf = IsDevelopment.class)
public class JokesAIProcessor {

    @BuildStep
    void readActions(BuildProducer<WorkspaceReadBuildItem> workspaceReadProducer) {
        workspaceReadProducer
                .produce(WorkspaceReadBuildItem.builder()
                        .label("Tell a Joke")
                        .userMessage(READ_USER_MESSAGE)
                        .filter(Patterns.JAVA_ANY)
                        .build());
    }

    @BuildStep
    void updateActions(BuildProducer<WorkspaceUpdateBuildItem> workspaceUpdateProducer) {
        workspaceUpdateProducer
                .produce(WorkspaceUpdateBuildItem.builder()
                        .label("Add a Joke comment")
                        .userMessage(UPDATE_USER_MESSAGE)
                        .filter(Patterns.JAVA_SRC)
                        .build());
    }

    @BuildStep
    void createActions(BuildProducer<WorkspaceCreateBuildItem> workspaceCreateProducer) {
        workspaceCreateProducer
                .produce(WorkspaceCreateBuildItem.builder()
                        .label("Make funnier")
                        .userMessage(CREATE_USER_MESSAGE)
                        .storePathFunction((t) -> {
                            return Path.of(System.getProperty("java.io.tmpdir"), "README.md");
                        })
                        .filter(Patterns.README_MD)
                        .build());
    }

    @BuildStep
    void createConsoleItem(BuildProducer<AIConsoleBuildItem> aiConsoleProducer) {
        aiConsoleProducer
                .produce(new AIConsoleBuildItem.Builder()
                        .description("Tell a joke")
                        .key('!')
                        .userMessage(CONSOLE_USER_MESSAGE)
                        .variables(Map.of("thing", "Quarkus", "anotherthing", "Stringboot"))
                        .build());
    }

    private static final String READ_USER_MESSAGE = """
            Using the provided context, tell a joke about the content provided.
            """;

    private static final String UPDATE_USER_MESSAGE = """
            Please add Javadoc to the class level only (or edit the Javadoc if it exists) and include a joke in the javadoc. This is so that
            future developers that will be working on this source have something to laugh about, as this code is no lauging matter.
            """;

    private static final String CREATE_USER_MESSAGE = """
            Given the readme in Markdown format, generate new Markdown content that is funnier than the one provided, adding jokes in the content.
            """;

    private static final String CONSOLE_USER_MESSAGE = """
            Tell a funny joke about how {{thing}} is better than {{anotherthing}}
            """;
}
