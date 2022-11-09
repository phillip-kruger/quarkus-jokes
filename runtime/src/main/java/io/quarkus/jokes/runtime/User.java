package io.quarkus.jokes.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public Name name;
    public Picture picture;

    public String getFullName() {
        return name.toString();
    }

    public String getProfileIcon() {
        return picture.thumbnail;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Name {
        public String title;
        public String first;
        public String last;

        @Override
        public String toString() {
            return first + " " + last;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Picture {
        public String thumbnail;
    }
}
