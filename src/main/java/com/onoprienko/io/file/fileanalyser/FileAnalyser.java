package com.onoprienko.io.file.fileanalyser;

import com.onoprienko.io.file.fileanalyser.entity.AnalyseInfo;
import com.onoprienko.io.file.fileanalyser.entity.RequestInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class FileAnalyser {
    private static final Pattern regex = Pattern.compile("[.!?]");

    public static AnalyseInfo analyse(String path, String word) {
        try {
            RequestInfo request = new RequestInfo(word, readContent(path));
            return new AnalyseInfo(getSentences(request), countWordRepetition(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int countWordRepetition(RequestInfo request) {
        int count = 0;
        int index = 0;
        String fileContent = request.getContent().toUpperCase();
        String word = request.getWord().toUpperCase();
        while (true) {
            index = fileContent.indexOf(word, index);
            if (index < 0) {
                break;
            }
            count++;
            index++;
        }
        return count;
    }

    static List<String> getSentences(RequestInfo request) {
        String[] sentences = regex.split(request.getContent());
        List<String> resultSentences = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.toUpperCase().contains(request.getWord().toUpperCase())) {
                sentence = sentence.trim();
                resultSentences.add(sentence);
            }
        }
        return resultSentences;
    }


    static String readContent(String path) throws IOException {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] text = new byte[(int) file.length()];
            inputStream.read(text);
            return new String(text);
        }
    }
}

