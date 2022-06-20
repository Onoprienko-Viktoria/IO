package com.onoprienko.io.stream;

import java.io.IOException;
import java.io.OutputStream;


public class BufferedOutputStream extends OutputStream {
    private byte[] buffer;
    private final OutputStream target;
    private int position;
    private int capacity;
    private final static int DEFAULT_CAPACITY = 5;

    public BufferedOutputStream(OutputStream target) {
        this.target = target;
        this.buffer = new byte[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
    }

    public BufferedOutputStream(OutputStream target, int capacity) {
        this.target = target;
        this.buffer = new byte[capacity];
        this.capacity = capacity;
    }

    @Override
    public void write(int b) throws IOException {
        buffer[position] = (byte) b;
        position++;
        if (position == capacity) {
            flush();
        }
    }


    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int endIndex = Math.min(capacity, len);
            System.arraycopy(b, off, buffer, position, endIndex);
            off += endIndex;
            len = len - endIndex;
            position = capacity;
            flush();
        }
    }

    @Override
    public void flush() throws IOException {
        target.write(buffer, 0, position);
        position = 0;
        super.flush();
    }

    @Override
    public void close() throws IOException {
        try (target) {
            flush();
        }
    }
}
