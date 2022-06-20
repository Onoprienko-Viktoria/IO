package com.onoprienko.io.stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class BufferedOutputStreamTest {
    private final static String FILE_PATH = "./src/test/resources/file.txt";

    @BeforeEach
    @SuppressWarnings("all")
    public void create() throws IOException {
        File file = new File(FILE_PATH);
        file.createNewFile();
    }

    @Test
    void writeContentWorkCorrect() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String text = "Test test test test test test test test test test test test test test test test " +
                    "test test test test test test test test test test test test";
            outputStream.write(text.getBytes(), 0, text.length());

            byte[] result = new byte[text.length()];
            int read = fileInputStream.read(result);

            assertTrue(read != -1);
            assertEquals(text, new String(result));
        }
    }

    @Test
    void writeContentFromStartToMiddleWorkCorrect() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String content = "Java test byte array output stream";
            int startIndex = 0;
            int endIndex = content.indexOf("output");
            outputStream.write(content.getBytes(), startIndex, endIndex);

            byte[] result = new byte[endIndex];
            int read = fileInputStream.read(result);

            assertTrue(read != -1);
            assertEquals("Java test byte array ", new String(result));
        }
    }

    @Test
    void writeContentFromMiddleWorkCorrect() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String content = "Java test byte array output stream";
            int startIndex = content.indexOf("byte");
            int endIndex = content.indexOf("stream") - startIndex;
            outputStream.write(content.getBytes(), startIndex, endIndex);

            byte[] result = new byte[endIndex];
            int read = fileInputStream.read(result);

            assertTrue(read != -1);
            assertEquals("byte array output ", new String(result));
        }
    }

    @Test
    void writeContentFromMiddleToEndCorrect() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String content = "Java test byte array output stream";
            int startIndex = content.indexOf("byte");
            int endIndex = content.length() - startIndex;
            outputStream.write(content.getBytes(), startIndex, endIndex);

            byte[] result = new byte[endIndex];
            int read = fileInputStream.read(result);

            assertNotEquals(-1, read);
            assertEquals("byte array output stream", new String(result));
        }
    }

    @Test
    void writeContentWithEndIndexReturnEmpty() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String content = "Java test byte array output stream";
            int startIndex = content.length();
            int endIndex = 0;
            outputStream.write(content.getBytes(), startIndex, endIndex);

            byte[] result = new byte[endIndex];
            int read = fileInputStream.read(result);

            assertTrue(read != -1);
            assertEquals("", new String(result));
        }
    }

    @Test
    void writeContentWithWrongIndexesReturnException() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            String content = "Java test byte array output stream";
            int startIndex = 1433;
            int endIndex = 1777;
            assertThrows(IndexOutOfBoundsException.class, () -> outputStream.write(content.getBytes(), startIndex, endIndex));

            byte[] result = new byte[endIndex];
            int read = fileInputStream.read(result);

            assertEquals(-1, read);
        }
    }

    @Test
    @SuppressWarnings("all")
    void writeNullValueReturnException() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {

            assertThrows(NullPointerException.class, () -> outputStream.write(null));

            byte[] result = new byte[1];
            int read = fileInputStream.read(result);

            assertEquals(-1, read);
        }
    }

    @Test
    void writeSingleChar() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            outputStream.write('A');
            outputStream.flush();

            byte[] result = new byte[1];
            int read = fileInputStream.read(result);

            assertTrue(read >= 0);
            assertEquals("A", new String(result));
        }
    }

    @Test
    void writeFewSingleChars() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH));
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            outputStream.write('A');
            outputStream.flush();
            outputStream.write('B');
            outputStream.flush();
            outputStream.write('C');
            outputStream.flush();

            byte[] result = new byte[3];
            int read = fileInputStream.read(result);

            assertTrue(read >= 0);
            assertEquals("ABC", new String(result));
        }
    }

    @Test
    void writeFewSingleCharsInBufferedOutputStreamWithOtherInitialCapacity() throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(FILE_PATH), 1);
             FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            outputStream.write('A');
            outputStream.write('B');
            outputStream.write('C');
            outputStream.write('D');
            outputStream.write('E');

            byte[] result = new byte[5];
            int read = fileInputStream.read(result);

            assertTrue(read >= 0);
            assertEquals("ABCDE", new String(result));
        }
    }


    @AfterEach
    @SuppressWarnings("all")
    public void remove() {
        File file = new File(FILE_PATH);
        file.delete();
    }
}