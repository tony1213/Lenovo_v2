package com.overtech.lenovo.config;

import android.util.Log;
/**
 * 用于开发过程中的调试
 * @author Overtech Will
 *
 */
public class Debug {
	public static boolean Debug=true;
	public static void log(String tag,String msg){
		if(Debug){
			Log.e(tag, msg);
		}
	}
}
