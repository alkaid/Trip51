// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.dianping.archive.*;

public class SimpleMsg
    implements Parcelable, Decoding
{

    public static final android.os.Parcelable.Creator CREATOR = new _cls2();
    public static final DecodingFactory DECODER = new _cls1();
    protected String content;
    protected String data;
    protected int flag;
    protected int icon;
    protected int statusCode;
    protected String title;

    protected SimpleMsg()
    {
    }

    public SimpleMsg(int i, String s, String s1, int j, int k, String s2)
    {
        statusCode = i;
        title = s;
        content = s1;
        icon = j;
        flag = k;
        data = s2;
    }

    protected SimpleMsg(Parcel parcel)
    {
        statusCode = parcel.readInt();
        title = parcel.readString();
        content = parcel.readString();
        icon = parcel.readInt();
        flag = parcel.readInt();
        data = parcel.readString();
    }

    public SimpleMsg(DPObject dpobject)
    {
        statusCode = dpobject.getInt(141);
        title = dpobject.getString(14057);
        content = dpobject.getString(22454);
        icon = dpobject.getInt(45243);
        flag = dpobject.getInt(29613);
        data = dpobject.getString(25578);
    }

    public SimpleMsg(String s, String s1, int i, int j)
    {
        title = s;
        content = s1;
        icon = i;
        flag = j;
    }

    public String content()
    {
        return content;
    }

    public String data()
    {
        return data;
    }

    public void decode(Unarchiver unarchiver)
        throws ArchiveException
    {
        do
        {
            int i = unarchiver.readMemberHash16();
            if(i <= 0)
                break;
            switch(i)
            {
            default:
                unarchiver.skipAnyObject();
                break;

            case 141:
                statusCode = unarchiver.readInt();
                break;

            case 14057:
                title = unarchiver.readString();
                break;

            case 22454:
                content = unarchiver.readString();
                break;

            case 45243:
                icon = unarchiver.readInt();
                break;

            case 29613:
                flag = unarchiver.readInt();
                break;

            case 25578:
                data = unarchiver.readString();
                break;
            }
        } while(true);
    }

    public int describeContents()
    {
        return 0;
    }

    public int flag()
    {
        return flag;
    }

    public int icon()
    {
        return icon;
    }

    public int statusCode()
    {
        return statusCode;
    }

    public String title()
    {
        return title;
    }

    public String toString()
    {
        return (new StringBuilder()).append(title).append(" : ").append(content).toString();
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(statusCode);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeInt(icon);
        parcel.writeInt(flag);
        parcel.writeString(data);
    }


    private class _cls1
        implements DecodingFactory
    {

        public SimpleMsg[] createArray(int i)
        {
            return new SimpleMsg[i];
        }

        public volatile Object[] createArray(int i)
        {
            return createArray(i);
        }

        public SimpleMsg createInstance(int i)
        {
            SimpleMsg simplemsg;
            if(i == 37021)
                simplemsg = new SimpleMsg();
            else
                simplemsg = null;
            return simplemsg;
        }

        public volatile Object createInstance(int i)
        {
            return createInstance(i);
        }

        _cls1()
        {
        }
    }


    private class _cls2
        implements android.os.Parcelable.Creator
    {

        public SimpleMsg createFromParcel(Parcel parcel)
        {
            return new SimpleMsg(parcel);
        }

        public volatile Object createFromParcel(Parcel parcel)
        {
            return createFromParcel(parcel);
        }

        public SimpleMsg[] newArray(int i)
        {
            return new SimpleMsg[i];
        }

        public volatile Object[] newArray(int i)
        {
            return newArray(i);
        }

        _cls2()
        {
        }
    }

}
