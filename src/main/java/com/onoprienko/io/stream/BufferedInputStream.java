package com.onoprienko.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class BufferedInputStream extends InputStream {
    private static final int DEFAULT_CAPACITY = 5;
    private final byte[] buffer;
    private int position;
    private final InputStream target;
    private final int capacity;

    @SuppressWarnings("all")
    public BufferedInputStream(InputStream target) throws IOException {
        this.target = target;
        this.buffer = new byte[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        target.read(buffer);
    }

    @SuppressWarnings("all")
    public BufferedInputStream(InputStream target, int capacity) throws IOException {
        this.target = target;
        this.buffer = new byte[capacity];
        this.capacity = capacity;
        target.read(buffer);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int startLen = len;
        while (true) {
            int endIndex = Math.min(capacity, len);
            System.arraycopy(buffer, position, b, off, endIndex);
            off += endIndex;
            len = len - endIndex;
            if (len <= 0) {
                break;
            }
            position = capacity;
            if (target.read(buffer) < 0) {
                return -1;
            }
        }
        return startLen;
    }

    @Override
    public void close() throws IOException {
        try (target) {
            Arrays.fill(buffer, (byte) 0);
            position = 0;
        }
    }

    @Override
    public int read() throws IOException {
        if (position >= buffer.length) {
            return -1;
        }
        if (position == capacity) {
            position = 0;
            if (target.read(buffer) < 0) {
                return -1;
            }
        }
        if (buffer[position] != 0) {
            int current = buffer[position];
            position++;
            return current;
        }
        return -1;
    }
}
