package io.quarkus.jokes.deployment.devui;

import java.io.IOException;
import java.io.InputStream;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.deployment.DevUIWebJarBuildItem;
import io.quarkus.maven.dependency.GACT;
import io.quarkus.vertx.http.deployment.webjar.WebJarBuildItem;
import io.quarkus.vertx.http.deployment.webjar.WebJarResourcesFilter;

/**
 * This example show how to show some pages in Dev UI
 */
public class JokesCommonProcessor {
    private static final GACT UI_JAR = new GACT("io.quarkiverse.jokes", "quarkus-jokes-common", null, "jar");
    private static final String DEVUI = "dev-ui";

    @BuildStep(onlyIf = IsDevelopment.class)
    void createShared(BuildProducer<WebJarBuildItem> webJarBuildProducer,
            BuildProducer<DevUIWebJarBuildItem> devUIWebJarProducer) {
        webJarBuildProducer.produce(WebJarBuildItem.builder()
                .artifactKey(UI_JAR)
                .root(DEVUI + "/")
                .filter(new WebJarResourcesFilter() {
                    @Override
                    public WebJarResourcesFilter.FilterResult apply(String fileName, InputStream file) throws IOException {
                        return new WebJarResourcesFilter.FilterResult(file, true);
                    }
                }).build());
        devUIWebJarProducer.produce(new DevUIWebJarBuildItem(UI_JAR, DEVUI));
    }
}
