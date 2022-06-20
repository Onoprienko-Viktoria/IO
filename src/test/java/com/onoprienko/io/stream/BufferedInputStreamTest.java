package com.onoprienko.io.stream;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BufferedInputStreamTest {

    @Test
    public void testReadSingleChar() throws IOException {
        byte[] content = new byte[]{'H'};
        BufferedInputStream byteArrayInputStream = new BufferedInputStream(new java.io.ByteArrayInputStream(content));
        assertEquals('H', (char) byteArrayInputStream.read());
        assertEquals(-1, byteArrayInputStream.read());
    }


    @Test
    public void testReadFewSingleChars() throws IOException {
        String content = "Hello";
        BufferedInputStream byteArrayInputStream = new BufferedInputStream(new java.io.ByteArrayInputStream(content.getBytes()));
        assertEquals('H', (char) byteArrayInputStream.read());
        assertEquals('e', (char) byteArrayInputStream.read());
        assertEquals('l', (char) byteArrayInputStream.read());
        assertEquals('l', (char) byteArrayInputStream.read());
        assertEquals('o', (char) byteArrayInputStream.read());
        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    public void testReadOnEmptyContentReturnMinusOne() throws IOException {
        String content = "";
        BufferedInputStream byteArrayInputStream = new BufferedInputStream(new java.io.ByteArrayInputStream(content.getBytes()));

        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    @SuppressWarnings("all")
    public void testReadContentWithNullValueWillThrowException() {
        assertThrows(NullPointerException.class, () -> new BufferedInputStream(new java.io.ByteArrayInputStream(null)));
    }


    @Test
    void testReadToArray() throws IOException {
        String content = "Java test byte array input stream";

        BufferedInputStream byteArrayInputStream = new BufferedInputStream(new java.io.ByteArrayInputStream(content.getBytes()), 100);

        byte[] result = new byte[content.length()];
        int readSize = byteArrayInputStream.read(result);
        assertNotEquals(-1, readSize);
        assertEquals(content, new String(result).trim());
    }

    @Test
    void testReadToSmallArray() throws IOException {
        String content = "Java test byte array input stream";

        BufferedInputStream byteArrayInputStream = new BufferedInputStream(new java.io.ByteArrayInputStream(content.getBytes()), 1);

        byte[] result = new byte[1];
        int readSize = byteArrayInputStream.read(result);
        assertNotEquals(-1, readSize);
        assertEquals("J", new String(result).trim());
    }

}

