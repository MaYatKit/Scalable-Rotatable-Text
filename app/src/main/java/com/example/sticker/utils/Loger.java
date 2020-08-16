package com.example.sticker.utils;

import android.util.Log;


import java.util.Arrays;

public class Loger {

    public static boolean sDebug = true;

    public static void setDEBUG(boolean debug) {
        sDebug = debug;
    }

    public static void v(String tag, String msg) {
        if (sDebug) {
            Log.v(tag, msg);
        }
    }


    public static void v(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.v(tag, msg, tr);
        }
    }


    public static void v(String tag, Object... msg) {
        if (sDebug) {
            Log.v(tag, Arrays.toString(msg));
        }
    }


    public static void d(String tag, String msg) {
        if (sDebug) {
            Log.d(tag, msg);
        }
    }


    public static void d(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.d(tag, msg, tr);
        }
    }


    public static void d(String tag, Object... msg) {
        if (sDebug) {
            Log.d(tag, Arrays.toString(msg));
        }
    }


    public static void i(String tag, String msg) {
        if (sDebug) {
            Log.i(tag, msg);
        }
    }


    public static void i(String tag, Object... msg) {
        if (sDebug) {
            Log.i(tag, Arrays.toString(msg));
        }
    }


    public static void i(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.i(tag, msg, tr);
        }
    }


    public static void w(String tag, String msg) {
        if (sDebug) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Object... msg) {
        if (sDebug) {
            Log.w(tag, Arrays.toString(msg));
        }
    }


    public static void w(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.w(tag, msg, tr);
        }
    }


    public static void e(String tag, String msg) {
        if (sDebug) {
            Log.e(tag, msg);
        }
    }


    public static void e(String tag, Object... msg) {
        if (sDebug) {
            Log.e(tag, Arrays.toString(msg));
        }
    }


    public static void e(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.e(tag, msg, tr);
        }
    }


    public static String where() {
        if (!sDebug) {
            return "";
        }

        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];

        return ste.toString();
    }

    public static void here() {
        if (!sDebug) {
            return;
        }

        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.

        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";

        Loger.d(Loger.class.getSimpleName(), info);
    }

    public static void here(final String tag) {
        if (!sDebug) {
            return;
        }

        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.

        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";

        Loger.d(tag, info);
    }


    public static void here(final String tag, final String msg) {
        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.

        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";

        Loger.d(tag, info + ": " + msg);
    }
}
