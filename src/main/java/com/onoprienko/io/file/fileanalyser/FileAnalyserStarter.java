package com.onoprienko.io.file.fileanalyser;

import com.onoprienko.io.file.fileanalyser.entity.RequestInfo;

import java.io.IOException;
import java.util.Scanner;

import static com.onoprienko.io.file.fileanalyser.FileAnalyser.*;

public class FileAnalyserStarter {
    private static final String INPUT_START = "java FileAnalyzer ";

    public static void startAnalyse() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Print file path and word:");
            String input = scanner.nextLine();

            RequestInfo request = validateRequest(input);
            System.out.println("Word repetition: " + countWordRepetition(request));
            System.out.println("Sentences with word " + request.getWord() + ":");
            for (String sentence : getSentences(request)) {
                System.out.println(sentence);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static RequestInfo validateRequest(String input) {
        try {
            if (input.contains(INPUT_START)) {
                String pathAndWord = input.substring(INPUT_START.length());
                String path = pathAndWord.substring(0, pathAndWord.indexOf(" "));
                String word = pathAndWord.substring(pathAndWord.indexOf(" ") + 1);
                String fileContent = readContent(path);
                return new RequestInfo(word, fileContent);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Exception while validate request: missing requested word or path to file");
        } catch (IOException e) {
            throw new RuntimeException("Exception while validate request: ", e);
        }
        throw new IllegalArgumentException("Request entered incorrectly. Try typing: \"java FileAnalyser *file path* *word*\"");
    }

}
