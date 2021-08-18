package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscordPost {
    private final String content;

    @JsonCreator
    public DiscordPost(@JsonProperty("content") final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static class Builder {
        private final StringBuilder stringBuilder = new StringBuilder();

        public synchronized Builder addLine(final String text) {
            stringBuilder.append("\n");
            stringBuilder.append(text);
            return this;
        }

        public DiscordPost build() {
            return new DiscordPost(stringBuilder.toString());
        }
    }
}
