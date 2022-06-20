package com.onoprienko.io.stream;

import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream {
    private final byte[] array;
    private int position;
    private final static int DEFAULT_CAPACITY = 1000;

    public ByteArrayOutputStream() {
        this.array = new byte[DEFAULT_CAPACITY];
    }

    public ByteArrayOutputStream(int capacity) {
        this.array = new byte[capacity];
    }

    @Override
    public void write(int b) {
        array[position] = (byte) b;
        position++;
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        System.arraycopy(b, off, array, position, len);
        position += len;
    }

    @Override
    public String toString() {
        return new String(array).substring(0, position);
    }
}
