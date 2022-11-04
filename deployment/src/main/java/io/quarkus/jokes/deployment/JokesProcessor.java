package io.quarkus.jokes.deployment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JokesProcessor {
    private static final String FEATURE = "dev-ui-example";
    private final ObjectMapper mapper = new ObjectMapper();

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    JokesBuildItem createJokes() {

        Joke[] jokes = getTenJokes();
        User[] users = getTenUsers();

        List<Joke> jokesAndPeople = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Joke joke = jokes[i];
            User user = users[i];

            // Add a random user
            joke.setUser(user.getFullName());
            joke.setProfilePic(user.getProfileIcon());

            // Add a timestamp
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
            timestamp = timestamp.substring(0, timestamp.lastIndexOf("."));
            joke.setTimestamp(timestamp);

            jokesAndPeople.add(joke);
        }

        return new JokesBuildItem(jokesAndPeople);

    }

    private Joke[] getTenJokes() {
        try {
            return mapper.readValue(new URL("https://official-joke-api.appspot.com/jokes/ten"), Joke[].class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private User[] getTenUsers() {
        try {
            return mapper.readValue(new URL("https://randomuser.me/api?results=10"), Results.class).results;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Results {
        public User[] results;
    }
}
