package com.tesla.teslainventoryservice.model;

public class SlackPost {
    private final String text;

    public SlackPost(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Builder {
        private final StringBuilder stringBuilder = new StringBuilder();

        public synchronized Builder addLine(final String text) {
            stringBuilder.append("\n");
            stringBuilder.append(text);
            return this;
        }

        public SlackPost build() {
            return new SlackPost(stringBuilder.toString());
        }
    }
}
