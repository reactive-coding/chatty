package com.reactiveapps.chatty.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class TelephoneUtils {

	private static PowerManager.WakeLock wakeLock = null;

	private final static String TAG = TelephoneUtils.class.getSimpleName();

	/**
	 * 获取deviceId
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String macAddress = getMac(context);
		if (!TextUtils.isEmpty(macAddress)) {
			return macAddress;
		}
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if (TextUtils.isEmpty(deviceId)) {
			return "";
		}
		return deviceId;
	}
	
	/**
	* Title: getMac
	* Description:
	* 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0 
	* @param context
	* @return
	* @author cuixiaoliang@gmail.com
	* @date 2015-4-15
	*/
	public static String getMac(Context context){
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		}
		return macAddress;
	}

	/**
	 * 唤醒屏幕
	 * 
	 * @param context
	 */
	public static void wakePower(Context context) {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			if (pm != null) {
				// PARTIAL_WAKE_LOCK 保持CPU 运转，屏幕和键盘灯有可能是关闭的
				// SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
				// SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
				// FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
						| PowerManager.ON_AFTER_RELEASE, TAG);
				if (wakeLock != null) {
					wakeLock.acquire();
				}
			}
		}

	}

	/**
	 * 取消屏幕唤醒
	 */
	public static void releasePower() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}

	}

	/**
	 * 获取手机品牌
	 * @return
	 */
	public static String getBrand(){
		return android.os.Build.BRAND;// 手机品牌
	}
	
	/**
	 * 手机型号
	 * 
	 * @return
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * android系统版本
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getSdkVersion() {
		return android.os.Build.VERSION.SDK;
	}

	/**
	 * android 系统型号
	 * 
	 * @return
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * @Methods: getLocalNumber
	 * @Description: TODO(获取当前的手机号)
	 * @param @param context
	 * @param @return
	 * @return String
	 * 
	 * @author cui.xiaoliang
	 * @date 2013-9-29 下午3:56:16
	 * @version 1.0.0
	 * 
	 */
	public static String getLocalNumber(Context context) {
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tManager.getLine1Number();
		return number;
	}

	/**
	 * 获取手机信息
	 */
	public static String getPhoneInfo(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String mtyb = android.os.Build.BRAND;// 手机品牌
		String mtype = android.os.Build.MODEL; // 手机型号
		String imei = tm.getDeviceId();
		String imsi = tm.getSubscriberId();
		String numer = tm.getLine1Number(); // 手机号码
		String serviceName = tm.getSimOperatorName(); // 运营商
		return "品牌: " + mtyb + "\n" + "型号: " + mtype + "\n" + "版本: Android "
				+ android.os.Build.VERSION.RELEASE + "\n" + "IMEI: " + imei
				+ "\n" + "IMSI: " + imsi + "\n" + "手机号码: " + numer + "\n"
				+ "运营商: " + serviceName + "\n";
	}

	/**
	 * 获取手机内存大小
	 * 
	 * @return
	 */
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
			Log.e(TAG, "getTotalMemory(),操作异常: ", e);
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取当前可用内存大小
	 * 
	 * @return
	 */
	public static String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return Formatter.formatFileSize(context, mi.availMem);
	}

	/**
	 * 获取手机CPU信息
	 * 
	 * @return
	 */
	public static String getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			Log.e(TAG, "getCpuInfo(),操作异常: ", e);
		}
		return "CPU型号: " + cpuInfo[0] + "\n" + "CPU频率: " + cpuInfo[1] + "\n";
	}
	
	public static String getVersionName(Context context) {
		String version = "1.0.0";
		try{
		// 获取packagemanager的实�?
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名�?代表是获取版本信�?
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		version = packInfo.versionName;
		}catch(NameNotFoundException e){
			Log.e(TAG,e.toString());
		}
		return version;
	}
	
	/** 
     * 获取网络状态，wifi,wap,2g,3g. 
     * 
     * @param context 上下文 
     * @return int 网络状态 
     * {@link #NETWORKTYPE_2G},
     * {@link #NETWORKTYPE_3G},          
     * {@link #NETWORKTYPE_INVALID},
     * {@link #NETWORKTYPE_WAP}
     * {@link #NETWORKTYPE_WIFI} 
     */  
  
    public static String getNetWorkType(Context context) {
  
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
  
        if (networkInfo != null && networkInfo.isConnected()) {  
            String type = networkInfo.getTypeName();
  
            if (type.equalsIgnoreCase("WIFI")) {  
                mNetWorkType = NETWORKTYPE_WIFI;  
            } else if (type.equalsIgnoreCase("MOBILE")) {  
                @SuppressWarnings("deprecation")
				String proxyHost = android.net.Proxy.getDefaultHost();
  
                mNetWorkType = TextUtils.isEmpty(proxyHost)?(isFastMobileNetwork(context)?NETWORKTYPE_3G:NETWORKTYPE_2G):NETWORKTYPE_WAP;
            }  
        } else {  
            mNetWorkType = NETWORKTYPE_INVALID;  
        }  
        switch(mNetWorkType){
        case NETWORKTYPE_3G:
        case NETWORKTYPE_2G:
        case NETWORKTYPE_WAP:
        	return "mobile";
        case NETWORKTYPE_INVALID:
        	return "other";
        case NETWORKTYPE_WIFI:
        	return "wifi";
        }
		return "other";
    }   
    
    /**
     * 判断是否是FastMobileNetWork，将3G或者3G以上的网络称为快速网络
     * @param context
     * @return
     */
    private static boolean isFastMobileNetwork(Context context) {
    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	switch (telephonyManager.getNetworkType()) {  
    	       case TelephonyManager.NETWORK_TYPE_1xRTT:
    	           return false; // ~ 50-100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_CDMA:
    	           return false; // ~ 14-64 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EDGE:
    	           return false; // ~ 50-100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_0:
    	           return true; // ~ 400-1000 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_A:
    	           return true; // ~ 600-1400 kbps  
    	       case TelephonyManager.NETWORK_TYPE_GPRS:
    	           return false; // ~ 100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_HSDPA:
    	           return true; // ~ 2-14 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_HSPA:
    	           return true; // ~ 700-1700 kbps  
    	       case TelephonyManager.NETWORK_TYPE_HSUPA:
    	           return true; // ~ 1-23 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_UMTS:
    	           return true; // ~ 400-7000 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EHRPD:
    	           return true; // ~ 1-2 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_B:
    	           return true; // ~ 5 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_HSPAP:
    	           return true; // ~ 10-20 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_IDEN:
    	           return false; // ~25 kbps  
    	       case TelephonyManager.NETWORK_TYPE_LTE:
    	           return true; // ~ 10+ Mbps  
    	       case TelephonyManager.NETWORK_TYPE_UNKNOWN:
    	           return false;  
    	       default:  
    	           return false;  
    	    }  
    }
    
	/**
	 * 
	 * @Methods: overrideGetSize 
	 * @Description: TODO(获取屏幕宽度，高�? 
	 * @param @param display
	 * @param @param outSize     
	 * @return void 
	 *
	 * @author cui.xiaoliang  
	 * @date 2013-9-24 下午7:47:32
	 * @version 1.0.0
	 * 
	 * Display display = getWindowManager().getDefaultDisplay();
	 * Point size = new Point();
	 * overrideGetSize(display, size);
	 * int width = size.x;
     * int height = size.y;
	 */
	@SuppressWarnings("deprecation")
	public static String getScreenHeightWight(Context context) {
		WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
	    try {
	      // test for new method to trigger exception
	      Class<?> pointClass = Class.forName("android.graphics.Point");
	      Method newGetSize = Display.class.getMethod("getSize", new Class[]{ pointClass });

	      // no exception, so new method is available, just use it
	      newGetSize.invoke(display, size);
	    } catch(Exception ex) {
	      // new method is not available, use the old ones
	    	size.x = display.getWidth();
	    	size.y = display.getHeight();
	    }
	    
	    return size.x+"*"+size.y;
	}
	
	/**
	  DisplayMetrics metric = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metric);
      int width = metric.widthPixels;  // 屏幕宽度（像素）
      int height = metric.heightPixels;  // 屏幕高度（像素）
      float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
      int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
	 */

	/**
	* <p>Title: dip2px</p>
	* <p>Description: </p>
	* @param context
	* @param dipValue
	* @return
	*/
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	* <p>Title: px2dip</p>
	* <p>Description: </p>
	* @param context
	* @param pxValue
	* @return
	*/
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;  
    /** 3G和3G以上网络，或统称为快速网络 */  
    public static final int NETWORKTYPE_3G = 3;  
    /** wifi网络 */  
    public static final int NETWORKTYPE_WIFI = 4;  
    
    private static int mNetWorkType;


	/**
	 * 获取包签名信息
	 */
	public static String getSignature(Context context){
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = null;
		StringBuilder sb = new StringBuilder();
		try {
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signature = pi.signatures;
			for (Signature tmp : signature) {
				sb.append(tmp.toCharsString());
			}
			if (!TextUtils.isEmpty(sb.toString())) {
				return sb.toString();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
