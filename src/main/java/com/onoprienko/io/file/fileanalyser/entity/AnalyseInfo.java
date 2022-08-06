package com.onoprienko.io.file.fileanalyser.entity;

import java.util.List;

public class AnalyseInfo {
    private final List<String> sentences;
    private final int count;

    public AnalyseInfo(List<String> sentences, int count) {
        this.sentences = sentences;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public List<String> getSentences() {
        return sentences;
    }
}
