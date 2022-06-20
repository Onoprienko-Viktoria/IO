package com.onoprienko.io.stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayInputStreamTest {

    @Test
    public void testReadSingleChar() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("H".getBytes());
        assertEquals('H', (char) byteArrayInputStream.read());
        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    public void testReadFewSingleChars() {
        String content = "Hello";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        assertEquals('H', (char) byteArrayInputStream.read());
        assertEquals('e', (char) byteArrayInputStream.read());
        assertEquals('l', (char) byteArrayInputStream.read());
        assertEquals('l', (char) byteArrayInputStream.read());
        assertEquals('o', (char) byteArrayInputStream.read());
        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    public void testReadOnEmptyContentReturnMinusOne() {
        String content = "";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    @SuppressWarnings("all")
    public void testReadContentWithNullValueWillThrowException() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(null);
        assertThrows(NullPointerException.class, () -> byteArrayInputStream.read());
    }


    @Test
    void testReadToArray() {
        String content = "Java test byte array input stream";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[content.length()];
        int readSize = byteArrayInputStream.read(result);
        assertNotEquals(-1, readSize);
        assertEquals(content, new String(result).trim());
    }

    @Test
    void testReadToSmallArray() {
        String content = "Java test byte array input stream";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[1];
        int readSize = byteArrayInputStream.read(result);
        assertNotEquals(-1, readSize);
        assertEquals("J", new String(result).trim());
    }

    @Test
    void testReadEmptyArray() {
        String content = "";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[1];
        int readSize = byteArrayInputStream.read(result);
        assertEquals(-1, readSize);
    }


    @Test
    void testReadFromStart() {
        String content = "Java test byte array input stream";
        int startIndex = 0;
        int endIndex = content.length();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[endIndex];
        int readSize = byteArrayInputStream.read(result, startIndex, endIndex);

        assertNotEquals(readSize, -1);
        assertEquals(content, new String(result));
    }

    @Test
    void testReadFromMiddleToEnd() {
        String content = "Java test byte array input stream";
        int startIndex = content.indexOf("byte");
        int endIndex = content.length() - startIndex;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[content.length()];
        int readSize = byteArrayInputStream.read(result, startIndex, endIndex);

        assertNotEquals(readSize, -1);
        assertEquals("Java test byte array in", new String(result).trim());
    }

    @Test
    void testReadIntoMiddle() {
        String content = "Java test byte array input stream";
        int startIndex = content.indexOf("byte");
        int endIndex = content.indexOf("input");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[content.length()];
        int readSize = byteArrayInputStream.read(result, startIndex, endIndex);

        assertNotEquals(readSize, -1);
        assertEquals("Java test byte array", new String(result).trim());
    }

    @Test
    void testReadFromEndReturnEmpty() {
        String content = "Java test byte array input stream";
        int startIndex = content.length();
        int endIndex = 0;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[endIndex];
        int readSize = byteArrayInputStream.read(result, startIndex, endIndex);

        assertNotEquals(readSize, -1);
        assertEquals("", new String(result));
    }

}