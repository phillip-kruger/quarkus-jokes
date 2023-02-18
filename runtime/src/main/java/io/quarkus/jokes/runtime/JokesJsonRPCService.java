package io.quarkus.jokes.runtime;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provide Jokes for JSON-RPC Endpoint
 */
public class JokesJsonRPCService {

    public Joke getJoke() {
        Joke joke = fetchRandomJoke();
        return joke;
    }

    private Joke fetchRandomJoke() {
        Joke joke = getRandomJoke();
        User user = getRandomUser();

        // Add a random user
        joke.setUser(user.getFullName());
        joke.setProfilePic(user.getProfileIcon());

        joke.setTimestamp(getTimestamp());

        return joke;
    }

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
        return timestamp.substring(0, timestamp.lastIndexOf("."));
    }

    private Joke getRandomJoke() {
        try {
            return mapper.readValue(new URL("https://official-joke-api.appspot.com/jokes/random/"), Joke.class);
        } catch (IOException ex) {
            // joke service not availalbe. Fallback to hardcoded
            return backupJoke;
        }
    }

    private User getRandomUser() {
        try {
            return mapper.readValue(new URL("https://randomuser.me/api"), Results.class).results[0];
        } catch (IOException ex) {
            // user service not availalbe. Fallback to hardcoded
            return backupUser;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Results {
        public User[] results;
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private static User backupUser = new User();
    private static Joke backupJoke = new Joke();
    static {
        backupUser.name = new User.Name();
        backupUser.name.first = "Phillip";
        backupUser.name.last = "Kruger";
        backupUser.name.title = "Mr.";
        backupUser.picture = new User.Picture();
        backupUser.picture.thumbnail = "https://avatars.githubusercontent.com/u/6836179?v=4";

        backupJoke.setId(0);
        backupJoke.setSetup("I'm afraid for the calendar.");
        backupJoke.setPunchline("Its days are numbered.");
    }
}
