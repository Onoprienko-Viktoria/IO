package com.onoprienko.io.file.fileanalyser.entity;

public class RequestInfo {
    private final String word;
    private final String content;

    public RequestInfo(String word, String content) {
        this.content = content;
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public String getWord() {
        return word;
    }
}
