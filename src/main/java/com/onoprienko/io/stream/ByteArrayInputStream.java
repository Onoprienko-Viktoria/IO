package com.onoprienko.io.stream;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputStream extends InputStream {
    private final byte[] contentBytes;
    private int position;
    private boolean isClosed = false;

    public ByteArrayInputStream(byte[] bytes) {
        contentBytes = bytes;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        if (isClosed) {
            throw new IOException("Stream is closed! ");
        }
        if (position == contentBytes.length) {
            return -1;
        }
        System.arraycopy(contentBytes, position, bytes, 0, bytes.length);
        position += bytes.length;
        return position == contentBytes.length ? -1 : bytes.length;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (isClosed) {
            throw new IOException("Stream is closed! ");
        }
        if (position == contentBytes.length) {
            return -1;
        }
        System.arraycopy(contentBytes, position, bytes, offset, length);
        position += length;
        return position == contentBytes.length ? -1 : bytes.length;
    }

    @Override
    public int read() throws IOException {
        if (isClosed) {
            throw new IOException("Stream is closed! ");
        }
        if (position >= contentBytes.length) {
            return -1;
        }
        byte value = contentBytes[position];
        position++;
        return value;
    }

    @Override
    public void close() {
        isClosed = true;
    }
}
