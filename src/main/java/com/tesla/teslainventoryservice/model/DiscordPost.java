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
        private boolean quote;

        public synchronized <T> Builder addLine(final String key, final T text) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("**").append(key).append(":** ").append(text);
            return this;
        }

        public synchronized <T> Builder addLine(final T text) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(text);
            return this;
        }

        public synchronized <T> Builder addLineIfNotNull(final String key, final T text) {

            if (text != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("**").append(key).append(":** ").append(text);
            }
            return this;
        }

        public Builder quote(final boolean quote) {
            this.quote = quote;
            return this;
        }

        public DiscordPost build() {
            if (quote) {
                return new DiscordPost(">>> " + stringBuilder);
            }
            return new DiscordPost(stringBuilder.toString());
        }
    }

    @Override
    public String toString() {
        return "DiscordPost{" +
                "content='" + content + '\'' +
                '}';
    }
}
