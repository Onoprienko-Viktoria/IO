package com.onoprienko.io.stream;

import java.io.IOException;
import java.io.OutputStream;


public class BufferedOutputStream extends OutputStream {
    private final byte[] buffer;
    private final OutputStream target;
    private int position;
    private final int capacity;
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
    public void write(byte[] bytes, int offset, int length) throws IOException {
        while (length > 0) {
            if (position == capacity) {
                flush();
            }
            int endIndex = Math.min(capacity, length);
            System.arraycopy(bytes, offset, buffer, position, endIndex);
            offset += endIndex;
            length = length - endIndex;
            position = endIndex;
        }
        flush();
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
