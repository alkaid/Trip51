package com.alkaid.trip51.base.dataservice.http.impl;

import java.io.IOException;
import java.io.InputStream;

public class WatchedInputStream extends InputStream {
    public static interface Listener {

        public void notify(int remains);
    }


    private Listener listener;
    private int notifyBytes;
    private int remains;
    private InputStream stream;

    public WatchedInputStream(InputStream inputstream, int notifyBytes) {
        stream = inputstream;
        this.notifyBytes = notifyBytes;
    }

    public int available()
            throws IOException {
        return stream.available();
    }

    public void close()
            throws IOException {
        stream.close();
    }

    /**
     * @deprecated Method mark is deprecated
     */

    public void mark(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean markSupported() {
        return false;
    }

    public int read()
            throws IOException {
        int i = 0;
        for (; i >= 0; i = stream.read()) {
            int k = 1 + remains;
            remains = k;
            if (k <= notifyBytes) {
                continue;
            }
        }
        int j = remains;
        remains = 0;
        if (listener != null)
            listener.notify(j);
        return i;
    }

    public int read(byte abyte0[])
            throws IOException {
        return read(abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
            throws IOException {
        int k = stream.read(abyte0, i, j);
        if (k < 0) {
            int j1 = remains;
            remains = 0;
            if (listener != null)
                listener.notify(j1);
        } else {
            remains = k + remains;
            if (k < 2048 && available() == 0) {
                int i1 = remains;
                remains = 0;
                if (listener != null)
                    listener.notify(i1);
            }
            if (remains > notifyBytes) {
                int l = remains;
                remains = remains % notifyBytes;
                if (listener != null)
                    listener.notify(l - remains);
            }
        }
        return k;

    }

    /**
     * @deprecated Method reset is deprecated
     */

    public void reset()
            throws IOException {
        throw new IOException("not supported operation: reset");
    }

    public void setListener(Listener listener1) {
        listener = listener1;
    }

    public long skip(long l)
            throws IOException {
        throw new IOException("not supported operation: skip");
    }
}
