package com.overtech.lenovo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.debug.Logger;

/**
 * @author Tony 2015-10-08
 */
public class Utilities {

    /**
     * 公用提示框
     *
     * @param message
     * @param context
     */
    public static void showToast(CharSequence message, Context context) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context.getApplicationContext(), message,
                duration);
        toast.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String setThroughTime() {
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat(("yyyy-MM-dd"));// 24小时制
        String LgTime = sdformat.format(date);
        return LgTime;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = inReader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static boolean checkSdCardIsExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSdCardPath() {
        // In this method,Lenovo pad return StorageDirectory,not "/mnt/sdcard1"
        String sdPath = null;
        if (checkSdCardIsExist()) {
            sdPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        }
        return sdPath.toString();
    }

    /**
     * 判断是否是Integer类型
     *
     * @param str
     * @return
     * @author daichangfu
     */
    public static boolean isNumber(String str) {
        if (str != null && !"".equals(str.trim())) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            Long number = 0l;
            if (isNum.matches()) {
                number = Long.parseLong(str);
            } else {
                return false;
            }
            if (number > 2147483647) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    // ^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9])).{6,}|(?:(?=.*[A-Z])(?=.*[a-z])|(?=.*[A-Z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])|(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[0-9])(?=.*[^A-Za-z0-9])|).{8,}

    // 获取网络状态和网络类型 create it at 2015-08-10 15:53
    public static String getNetworkStateName(Context context) {
        if (isNetworkConnected(context)) {
            int netWorkType = getConnectedType(context);
            if (netWorkType == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            } else if (netWorkType == ConnectivityManager.TYPE_MOBILE) {
                return "3G";
            } else {
                return "其他方式";
            }
        } else {
            return "无网络";
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;
    }

    // 手机号码正则表达式
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // 密码强度正则表达式
    public static boolean isPasswordStronger(String password) {
        String string = "^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9])).{6,}|(?:(?=.*[A-Z])(?=.*[a-z])|(?=.*[A-Z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])|(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[0-9])(?=.*[^A-Za-z0-9])|).{8,}";
        Pattern p = Pattern.compile(string);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    //判断是否为今天
    public static boolean isToday(Calendar cal) {
        boolean ret = false;
        Calendar tmp = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s1 = format.format(cal.getTime());
        String s2 = format.format(tmp.getTime());
        if (s1.equals(s2)) {
            ret = true;
        }
        return ret;
    }

    public static boolean isToday(String s1) {
        boolean ret = false;
        Calendar tmp = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s2 = format.format(tmp.getTime());
        if (s1.equals(s2)) {
            ret = true;
        }
        return ret;
    }

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 计算两个时间戳之间的时间差
     *
     * @param curTime
     * @param desTime
     * @return
     */
    public static String getTimeBetween(long curTime, long desTime) {
        Logger.e("当前时间戳" + curTime + "===" + "预约时间戳" + desTime);
        StringBuilder sb = new StringBuilder();
        long a = desTime - curTime;
        if (a < 0) {
            return "已到场";
        }
        int day = (int) a / 3600 / 24;
        if (day >= 1) {
            sb.append(day + "天");
        }
        int hour = (int) (a % (3600 * 24) / 3600);
        if (hour >= 1) {
            sb.append(hour + "小时");
            return sb.append("上门").toString();
        } else {
            if (day == 0) {
                return sb.append("1小时上门").toString();
            } else {
                return sb.append("上门").toString();
            }
        }
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight();
            int right=ScreenTools.instance(v.getContext()).dip2px(360);
            if(v.getParent() instanceof LinearLayout){//此处这样写是因为实际处理时发现点击评论框旁边的按钮时 无法发送数据直接就隐藏了
                right=((LinearLayout)v.getParent()).getWidth();
            }
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public static void hideSoftInput(IBinder token, Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);

            if(context instanceof MainActivity){//顺便隐藏一下输入框
                ((MainActivity)context).informationFragment.llCommentUpContainer.setVisibility(View.GONE);
            }
        }
    }
    public static void showSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
