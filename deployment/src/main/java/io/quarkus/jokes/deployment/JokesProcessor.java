package io.quarkus.jokes.deployment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.jokes.runtime.Joke;
import io.quarkus.jokes.runtime.User;

class JokesProcessor {
    private static final String FEATURE = "jokes";
    public static final Random RANDOM = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = Logger.getLogger(JokesProcessor.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    JokesBuildItem createJokes() {

        Joke[] jokes = getTenJokes();
        User[] users = getTenUsers();

        List<Joke> jokesAndPeople = new ArrayList<>();
        for (int i = 0; i < jokes.length; i++) {
            Joke joke = jokes[i];

            // Add a random user
            if (users.length > 0) {
                User user = users[RANDOM.nextInt(users.length)];
                joke.setUser(user.getFullName());
                joke.setProfilePic(user.getProfileIcon());
            }

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
            log.warn(ex);
            Joke joke = new Joke();
            joke.setSetup("What do you call an offline server?");
            joke.setPunchline("A toaster.");
            return new Joke[] { joke };
        }
    }

    private User[] getTenUsers() {
        try {
            return mapper.readValue(new URL("https://randomuser.me/api?results=10"), Results.class).results;
        } catch (IOException ex) {
            log.warn(ex);

            return new User[] {};
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Results {
        public User[] results;
    }
}
