package com.tesla.teslainventoryservice.model;

public class SlackPost {
    private final String text;

    public SlackPost(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
