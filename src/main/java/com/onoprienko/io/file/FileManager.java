package com.onoprienko.io.file;

import java.io.*;

@SuppressWarnings("all")
public class FileManager {

    public static int countFiles(String path) {
        int counter = 0;
        File directoryPath = new File(path);
        File[] files = directoryPath.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                counter++;
            } else {
                counter += countFiles(file.getPath());
            }
        }
        return counter;
    }

    public static int countDirs(String path) throws IOException {
        File rootDir = new File(path);
        int result = 0;
        if (rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    result += 1 + FileManager.countDirs(file.getCanonicalPath());
                }
            }
        }
        return result;
    }

    public static void copy(String from, String to) throws IOException {
        File file = new File(from);
        File[] files = file.listFiles();

        if (files != null) {
            copyDirectories(to, files);
        } else {
            File fileTo = new File(to);
            copyFile(file.getPath(), fileTo);
        }
    }


    private static void copyDirectories(String to, File[] files) throws IOException {
        for (File fileToCopy : files) {
            File fileTo = new File(to + fileToCopy.separator + fileToCopy.getName());
            if (fileToCopy.isDirectory()) {
                fileTo.mkdir();
                copy(fileToCopy.getPath(), to + fileToCopy.separator + fileToCopy.getName());
            } else if (fileToCopy.isFile()) {
                fileTo.createNewFile();
                copyFile(fileToCopy.getPath(), fileTo);
            }
        }
    }

    private static void copyFile(String from, File fileTo) throws IOException {
        File fileFrom = new File(from);
        fileTo.createNewFile();
        try (InputStream inputStream = new FileInputStream(fileFrom);
             OutputStream outputStream = new FileOutputStream(fileTo)) {
            int length = (int) fileFrom.length();
            byte[] content = new byte[length];
            inputStream.read(content);
            outputStream.write(content);
        }
    }


    public static void move(String from, String to) throws IOException {
        File fileToMove = new File(from);
        File[] files = fileToMove.listFiles();
        if (files != null) {
            for (File file : files) {
                File fileTo = new File(to + file.separator + file.getName());
                if (file.isDirectory()) {
                    file.mkdir();
                    move(file.getPath(), to + file.separator + file.getName());
                } else if (file.isFile()) {
                    file.createNewFile();
                    new File(file.getPath()).renameTo(fileTo);
                }
            }
        }
        new File(from).renameTo(new File(to));
    }
}
