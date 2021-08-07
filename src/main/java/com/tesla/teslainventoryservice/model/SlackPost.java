package com.tesla.teslainventoryservice.model;

public class SlackPost {
    private String text;

    public SlackPost(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
