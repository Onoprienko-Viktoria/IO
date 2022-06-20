package com.onoprienko.io.stream;

import java.io.InputStream;

public class ByteArrayInputStream extends InputStream {
    private final byte[] array;
    private int position;

    public ByteArrayInputStream(byte[] bytes) {
        array = bytes;
    }

    @Override
    public int read(byte[] b) {
        if (array.length == 0) {
            return -1;
        }
        System.arraycopy(array, position, b, 0, b.length);
        position = b.length;
        return array.length;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(array, position, b, off, len);
        position += len;
        return len;
    }

    @Override
    public int read() {
        if (position >= array.length) {
            return -1;
        }
        byte value = array[position];
        position++;
        return value;
    }
}
