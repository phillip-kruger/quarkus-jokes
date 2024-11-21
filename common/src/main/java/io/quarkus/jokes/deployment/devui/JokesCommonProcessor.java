package io.quarkus.jokes.deployment.devui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.deployment.BuildTimeConstBuildItem;
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
            BuildProducer<DevUIWebJarBuildItem> devUIWebJarProducer,
            BuildProducer<BuildTimeConstBuildItem> buildTimeConstProducer) {

        String namespace = UI_JAR.getGroupId() + "." + UI_JAR.getArtifactId();

        // Build Time data (under the common namespace, so usable in the common
        // This will allow you to access someKey in js:
        // TODO: Add example
        Map<String, Object> buildTimeData = new HashMap<>();
        buildTimeData.put("someKey", "value from common"); // This value can be an object, as long as this can serialize to json

        BuildTimeConstBuildItem item = new BuildTimeConstBuildItem(namespace, buildTimeData);
        buildTimeConstProducer.produce(item);

        String buildTimeDataImport = namespace + "-data";

        webJarBuildProducer.produce(WebJarBuildItem.builder()
                .artifactKey(UI_JAR)
                .root(DEVUI + "/")
                .filter(new WebJarResourcesFilter() {
                    @Override
                    public WebJarResourcesFilter.FilterResult apply(String fileName, InputStream file) throws IOException {

                        // If you want to use import { someKey } from `build-time-data`; you need this below:
                        if (fileName.endsWith(".js")) {
                            String content = new String(file.readAllBytes(), StandardCharsets.UTF_8);
                            content = content.replaceAll("build-time-data", buildTimeDataImport); // This part replace `build-time-data` with `io.quarkiverse.jokes.quarkus-jokes-common-data`
                            return new WebJarResourcesFilter.FilterResult(
                                    new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), true);
                        }

                        return new WebJarResourcesFilter.FilterResult(file, false);

                        // Your other option is to keep this simple and import the known name, so import { someKey } from `io.quarkiverse.jokes.quarkus-jokes-common-data`;
                        // return new WebJarResourcesFilter.FilterResult(file, true);

                        // You can also always import any build-time-data if you know the namespace, example, you can import common build-time-data in your extension (not common) same as above.

                    }
                }).build());
        devUIWebJarProducer.produce(new DevUIWebJarBuildItem(UI_JAR, DEVUI));
    }
}
