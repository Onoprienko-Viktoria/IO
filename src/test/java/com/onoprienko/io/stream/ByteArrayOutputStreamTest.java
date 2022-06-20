package com.onoprienko.io.stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteArrayOutputStreamTest {

    @Test
    void writeSingleCharWorkCorrect() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write('A');
        assertEquals("A", outputStream.toString());
    }

    @Test
    void writeFewSingleCharsWorkCorrect() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write('A');
        outputStream.write('B');
        outputStream.write('C');
        assertEquals("ABC", outputStream.toString());
    }

    @Test
    void writeNullThrowNullPointerException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(NullPointerException.class, () -> outputStream.write(null));
    }

    @Test
    void writeArrayOfBytesWorkCorrect() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Test content";
        outputStream.write(content.getBytes());
        assertEquals(content, outputStream.toString());
    }

    @Test
    void writeFromMiddleOfContentToContentLength() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Java test byte array output stream";
        int startIndex = content.indexOf("array");
        int endIndex = content.length() - startIndex;
        outputStream.write(content.getBytes(), startIndex, endIndex);
        assertEquals("array output stream", outputStream.toString());
    }


    @Test
    void writeFromMiddleOfContent() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Java test byte array output stream";
        int startIndex = content.indexOf("array");
        int endIndex = content.indexOf("stream") - startIndex;
        outputStream.write(content.getBytes(), startIndex, endIndex);
        assertEquals("array output ", outputStream.toString());
    }

    @Test
    void writeFromEndOfContentReturnEmpty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Java test byte array output stream";
        int startIndex = content.length();
        int endIndex = 0;
        outputStream.write(content.getBytes(), startIndex, endIndex);
        assertEquals("", outputStream.toString());
    }


    @Test
    void writeFromStartOfContentToMiddle() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Java test byte array output stream";
        int startIndex = 0;
        int endIndex = content.indexOf("output");
        outputStream.write(content.getBytes(), startIndex, endIndex);
        assertEquals("Java test byte array ", outputStream.toString());
    }

    @Test
    void writeWithWrongEndAndStartIndexesThrowIndexOutOfBounds() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "Java test byte array output stream";
        int startIndex = 1413;
        int endIndex = 2432;
        assertThrows(IndexOutOfBoundsException.class, () -> outputStream.write(content.getBytes(), startIndex, endIndex));
    }

    @Test
    void createByteArrayOutputStreamWithOtherInitialCapacity() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(100);
        String content = "Java test byte array output stream";
        int startIndex = content.indexOf("array");
        int endIndex = content.indexOf("stream") - startIndex;
        outputStream.write(content.getBytes(), startIndex, endIndex);
        assertEquals("array output ", outputStream.toString());
    }

    @Test
    void createByteArrayOutputStreamWithOtherInitialCapacityReturnExceptionIfSizeBiggerThenCapacity() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(5);
        String content = "Java test byte array output stream";
        int startIndex = content.indexOf("array");
        int endIndex = content.indexOf("stream") - startIndex;
        assertThrows(IndexOutOfBoundsException.class, () -> outputStream.write(content.getBytes(), startIndex, endIndex));

    }
}