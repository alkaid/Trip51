// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils
{
//    private static class SoftKeyboardHandler extends Handler
//        implements SoftKeyboardController
//    {
//
//        final boolean beforeCupcake = KeyboardUtils.beforeCupcake();
//        int count;
//        final View view;
//
//        public void handleMessage(Message message)
//        {
//            if(beforeCupcake)
//                return;
//            switch(message.what)
//            {
//            default:
//                break;
//            case 1: // '\001'
//                count = 0;
//                removeMessages(2);
//                removeMessages(4);
//                sendEmptyMessageDelayed(2, 200L);
//break;
//            case 2: // '\002'
//                if(view.isShown())
//                {
//                    boolean flag;
//                    try
//                    {
//                        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//                        for(int i=count;i<10;i++){
//                            flag=inputMethodManager.isActive()
//                        }
//                        int i = count;
//                        count = i + 1;
//                        if(i > 10)
//                            continue; /* Loop/switch isn't completed */
//                        flag = ((Boolean)inputMethodManager.getClass().getDeclaredMethod("isActive", new Class[0]).invoke(inputMethodManager, new Object[0])).booleanValue();
//                    }
//                    catch(Exception exception1)
//                    {
//                        exception1.printStackTrace();
//                        continue; /* Loop/switch isn't completed */
//                    }
//                    if(flag)
//                        continue; /* Loop/switch isn't completed */
//                }
//                sendEmptyMessageDelayed(2, 100L);
//                continue; /* Loop/switch isn't completed */
//
//            case 3: // '\003'
//                count = 0;
//                removeMessages(2);
//                removeMessages(4);
//                sendEmptyMessageDelayed(4, 200L);
//                break;
//
//            case 4: // '\004'
//                try
//                {
//                    Object obj = view.getContext().getSystemService("input_method");
//                    Class class1 = obj.getClass();
//                    Class aclass[] = new Class[2];
//                    aclass[0] = android/os/IBinder;
//                    aclass[1] = Integer.TYPE;
//                    Method method = class1.getDeclaredMethod("hideSoftInputFromWindow", aclass);
//                    Object aobj[] = new Object[2];
//                    aobj[0] = view.getWindowToken();
//                    aobj[1] = Integer.valueOf(2);
//                    method.invoke(obj, aobj);
//                }
//                catch(Exception exception)
//                {
//                    exception.printStackTrace();
//                }
//                break;
//            }
//            if(true) goto _L1; else goto _L3
//_L3:
//        }
//
//        public void hide()
//        {
//            sendEmptyMessage(3);
//        }
//
//        public void show()
//        {
//            sendEmptyMessage(1);
//        }
//
//        public SoftKeyboardHandler(View view1)
//        {
//            count = 0;
//            view = view1;
//        }
//    }

    public static interface SoftKeyboardController
    {

        public abstract void hide();

        public abstract void show();
    }


    public KeyboardUtils()
    {
    }

    public static boolean beforeCupcake()
    {
        boolean flag;
        if("2".equals(android.os.Build.VERSION.SDK) || "1".equals(android.os.Build.VERSION.SDK))
            flag = true;
        else
            flag = false;
        return flag;
    }

//    public static SoftKeyboardController getSoftKeyboardController(View view)
//    {
//        return new SoftKeyboardHandler(view);
//    }

    public static void hideKeyboard(View view)
    {
        ((InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void popupKeyboard(final View view)
    {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
                ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300L);
    }
}
