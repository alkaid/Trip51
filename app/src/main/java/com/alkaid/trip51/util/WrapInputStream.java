package com.alkaid.trip51.util;

import java.io.IOException;
import java.io.InputStream;

public abstract class WrapInputStream extends InputStream {

    private IOException ex;
    private InputStream ins;

    public WrapInputStream() {
    }

    /**
     * @deprecated Method inputStream is deprecated
     */

    private InputStream inputStream()
            throws IOException {
        if (null == ins) {
            ins = wrappedInputStream();
        }
        return ins;
    }

    public int available()
            throws IOException {
        return inputStream().available();
    }

    public void close()
            throws IOException {
        if (ins != null)
            inputStream().close();
    }

    public void mark(int i) {
    }

    public boolean markSupported() {
        return true;
    }

    public int read()
            throws IOException {
        return inputStream().read();
    }

    public int read(byte abyte0[])
            throws IOException {
        return inputStream().read(abyte0);
    }

    public int read(byte abyte0[], int i, int j)
            throws IOException {
        return inputStream().read(abyte0, i, j);
    }

    /**
     * @deprecated Method reset is deprecated
     */

    public void reset()
            throws IOException {
        ins = null;
    }

    public long skip(long l)
            throws IOException {
        return inputStream().skip(l);
    }

    protected abstract InputStream wrappedInputStream()
            throws IOException;
}
