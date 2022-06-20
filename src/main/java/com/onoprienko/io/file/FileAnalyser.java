package com.onoprienko.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FileAnalyser {

    private static final String INPUT_START = "java FileAnalyzer ";

    public static void analyse() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Print file path and word:");
            String input = scanner.nextLine();

            RequestInfo request = validateRequest(input);
            System.out.println("Word repetition: " + countWordRepetition(request));
            System.out.println("Sentences with word " + request.word + ":");
            for (String sentence : getSentences(request)) {
                System.out.println(sentence);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static AnalyseInfo analyse(String path, String word) {
        try {
            RequestInfo request = new RequestInfo(word, readContent(path));
            return new AnalyseInfo(getSentences(request), countWordRepetition(request));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static RequestInfo validateRequest(String input) {
        try {
            if (input.contains(INPUT_START)) {
                String pathAndWord = input.substring(INPUT_START.length());
                String path = pathAndWord.substring(0, pathAndWord.indexOf(" "));
                String word = pathAndWord.substring(pathAndWord.indexOf(" ") + 1);
                String fileContent = readContent(path);
                return new RequestInfo(word, fileContent);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while validate request: " + e.getMessage());
        }
        throw new IllegalArgumentException("Request entered incorrectly. Try typing: \"java FileAnalyser *file path* *word*\"");
    }

    private static int countWordRepetition(RequestInfo request) {
        int count = 0;
        int index = 0;
        String fileContent = request.content.toUpperCase();
        String word = request.word.toUpperCase();
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

    private static List<String> getSentences(RequestInfo request) {
        String[] sentences = request.content.split("[.!?]");
        List<String> resultSentences = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.toUpperCase().contains(request.word.toUpperCase())) {
                sentence = sentence.startsWith(" ") ? sentence.substring(1) : sentence;
                resultSentences.add(sentence);
            }
        }
        return resultSentences;
    }

    @SuppressWarnings(value = "all")
    private static String readContent(String path) throws IOException {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] text = new byte[(int) file.length()];
            inputStream.read(text);
            return new String(text);
        }
    }

    @SuppressWarnings("all")
    private static class RequestInfo {
        private final String word;
        private final String content;

        RequestInfo(String word, String content) {
            this.content = content;
            this.word = word;
        }
    }

    @SuppressWarnings("all")
    public static class AnalyseInfo {
        private final List<String> sentences;
        private final int count;

        AnalyseInfo(List<String> sentences, int count) {
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
}

