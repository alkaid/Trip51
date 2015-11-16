// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.util;

import java.io.IOException;
import java.io.InputStream;

public abstract class WrapInputStream extends InputStream
{

    private IOException ex;
    private InputStream ins;

    public WrapInputStream()
    {
    }

    /**
     * @deprecated Method inputStream is deprecated
     */

    private InputStream inputStream()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        if(ex != null)
            throw ex;
        break MISSING_BLOCK_LABEL_19;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        InputStream inputstream = ins;
        if(inputstream != null)
            break MISSING_BLOCK_LABEL_36;
        ins = wrappedInputStream();
        InputStream inputstream1 = ins;
        this;
        JVM INSTR monitorexit ;
        return inputstream1;
        IOException ioexception;
        ioexception;
        ex = ioexception;
        throw ex;
    }

    public int available()
        throws IOException
    {
        return inputStream().available();
    }

    public void close()
        throws IOException
    {
        if(ins != null)
            inputStream().close();
    }

    public void mark(int i)
    {
    }

    public boolean markSupported()
    {
        return true;
    }

    public int read()
        throws IOException
    {
        return inputStream().read();
    }

    public int read(byte abyte0[])
        throws IOException
    {
        return inputStream().read(abyte0);
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        return inputStream().read(abyte0, i, j);
    }

    /**
     * @deprecated Method reset is deprecated
     */

    public void reset()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        ins = null;
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public long skip(long l)
        throws IOException
    {
        return inputStream().skip(l);
    }

    protected abstract InputStream wrappedInputStream()
        throws IOException;
}
