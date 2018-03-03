package com.example.powerbbs.tackle;

import android.util.Log;

/**
 * Created by 晞余 on 2018/2/25.
 * 定制Log，方便控制打印
 */

public class Mog {
    public static final int level=1;
    public static final int  VERBOSE=1;
    public static final int  DEBUG=2;
    public static final int  INFO=3;
    public static final int  WARN=4;
    public static final int  ERROR=5;
    public static final int  NOTHING=6;
//    public static final int  =7;

    public static void v(String tag,String msg){
        if(level<= VERBOSE){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag,String msg){
        if(level<= DEBUG){
            Log.d(tag, msg);
        }
    }
    public static void i(String tag,String msg){
        if(level<= INFO){
            Log.i(tag, msg);
        }
    }
    public static void w(String tag,String msg){
        if(level<= WARN){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag,String msg){
        if(level<= ERROR){
            Log.e(tag, msg);
        }
    }
//    public static void n(String tag,String msg){
//        if(level<= NOTHING){
//            Log.v(tag, msg);
//        }
//    }

}
