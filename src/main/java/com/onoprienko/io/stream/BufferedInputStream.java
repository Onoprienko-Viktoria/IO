package com.onoprienko.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class BufferedInputStream extends InputStream {
    private static final int DEFAULT_CAPACITY = 5;
    private final byte[] buffer;
    private int position;
    private final InputStream target;
    private int capacity;

    public BufferedInputStream(InputStream target) {
        this.target = target;
        this.buffer = new byte[DEFAULT_CAPACITY];
    }

    public BufferedInputStream(InputStream target, int capacity) {
        this.target = target;
        this.buffer = new byte[capacity];
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        int startLen = length;
        while (true) {
            if (position == capacity || capacity == 0) {
                if (target.read(buffer) < 0) {
                    return -1;
                }
                position = 0;
                capacity = buffer.length;
            }
            int endIndex = Math.min(capacity, length);
            System.arraycopy(buffer, position, bytes, offset, endIndex);

            offset += endIndex;
            length -= endIndex;
            position += endIndex;
            if (length <= 0) {
                break;
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
