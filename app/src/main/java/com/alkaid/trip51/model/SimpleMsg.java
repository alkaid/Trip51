// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleMsg
    implements Parcelable/*, Decoding*/
{

    public static final android.os.Parcelable.Creator<SimpleMsg> CREATOR = new Creator<SimpleMsg>() {
        @Override
        public SimpleMsg createFromParcel(Parcel source) {
            return new SimpleMsg(source);
        }
        @Override
        public SimpleMsg[] newArray(int size) {
            return new SimpleMsg[size];
        }
    };
//    public static final DecodingFactory DECODER = new _cls1();
    protected String content;
    protected String data;
    protected int flag;
    protected int icon;
    protected int statusCode;
    protected String title;

    protected SimpleMsg()
    {
    }

    public SimpleMsg(int statusCode, String title, String s1, int icon, int flag, String data)
    {
        this.statusCode = statusCode;
        this.title = title;
        content = s1;
        this.icon = icon;
        this.flag = flag;
        this.data = data;
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

//    public SimpleMsg(DPObject dpobject)
//    {
//        statusCode = dpobject.getInt(141);
//        title = dpobject.getString(14057);
//        content = dpobject.getString(22454);
//        icon = dpobject.getInt(45243);
//        flag = dpobject.getInt(29613);
//        data = dpobject.getString(25578);
//    }

    public SimpleMsg(String title, String content, int icon, int flag)
    {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.flag = flag;
    }

    public String content()
    {
        return content;
    }

    public String data()
    {
        return data;
    }

//    public void decode(Unarchiver unarchiver)
//        throws ArchiveException
//    {
//        do
//        {
//            int i = unarchiver.readMemberHash16();
//            if(i <= 0)
//                break;
//            switch(i)
//            {
//            default:
//                unarchiver.skipAnyObject();
//                break;
//
//            case 141:
//                statusCode = unarchiver.readInt();
//                break;
//
//            case 14057:
//                title = unarchiver.readString();
//                break;
//
//            case 22454:
//                content = unarchiver.readString();
//                break;
//
//            case 45243:
//                icon = unarchiver.readInt();
//                break;
//
//            case 29613:
//                flag = unarchiver.readInt();
//                break;
//
//            case 25578:
//                data = unarchiver.readString();
//                break;
//            }
//        } while(true);
//    }

    @Override
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

    @Override
    public String toString()
    {
        return (new StringBuilder()).append(title).append(" : ").append(content).toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeInt(statusCode);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeInt(icon);
        parcel.writeInt(flag);
        parcel.writeString(data);
    }


//    private class _cls1
//        implements DecodingFactory
//    {
//
//        public SimpleMsg[] createArray(int i)
//        {
//            return new SimpleMsg[i];
//        }
//
//        public volatile Object[] createArray(int i)
//        {
//            return createArray(i);
//        }
//
//        public SimpleMsg createInstance(int i)
//        {
//            SimpleMsg simplemsg;
//            if(i == 37021)
//                simplemsg = new SimpleMsg();
//            else
//                simplemsg = null;
//            return simplemsg;
//        }
//
//        public volatile Object createInstance(int i)
//        {
//            return createInstance(i);
//        }
//
//        _cls1()
//        {
//        }
//    }
}
