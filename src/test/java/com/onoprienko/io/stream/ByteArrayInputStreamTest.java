package com.onoprienko.io.stream;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayInputStreamTest {

    @Test
    public void testReadSingleChar() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("H".getBytes());
        assertEquals('H', (char) byteArrayInputStream.read());
        assertEquals(-1, byteArrayInputStream.read());
    }

    @Test
    public void testReadFewSingleChars() throws IOException {
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
    public void testReadOnEmptyContentReturnMinusOne() throws IOException {
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
    void testReadToArray() throws IOException {
        String content = "Java test byte array input stream";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[content.length()];
        int readSize = byteArrayInputStream.read(result);
        assertEquals(-1, readSize);
        assertEquals(content, new String(result).trim());
    }

    @Test
    void testReadToSmallArray() throws IOException {
        String content = "Java test byte array input stream";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[1];
        int readSize = byteArrayInputStream.read(result);
        assertNotEquals(-1, readSize);
        assertEquals("J", new String(result).trim());
    }

    @Test
    void testReadEmptyArray() throws IOException {
        String content = "";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        byte[] result = new byte[1];
        int readSize = byteArrayInputStream.read(result);
        assertEquals(-1, readSize);
    }


    @Test
    void testReadFromStart() throws IOException {
        String content = "Java test byte array input stream";
        int startIndex = 0;
        int endIndex = content.length();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[endIndex];
        int readSize = byteArrayInputStream.read(result, startIndex, endIndex);

        assertEquals(readSize, -1);
        assertEquals(content, new String(result));
    }

    @Test
    void testReadIntoMiddleToEnd() throws IOException {
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
    void testReadIntoMiddle() throws IOException {
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
    void testReadToEndReturnEmpty() throws IOException {
        String content = "Java test byte array input stream";
        int startIndex = content.length();
        int endIndex = 0;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        byte[] result = new byte[endIndex];
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> byteArrayInputStream.read(result, startIndex, endIndex));
        assertEquals("arraycopy: last destination index 33 out of bounds for byte[0]",
                exception.getMessage());
    }

}