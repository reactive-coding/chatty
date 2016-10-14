package com.reactiveapps.chatty.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.reactiveapps.chatty.App;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class NetUtils {
	private static final String TAG = NetUtils.class.getSimpleName();

	/**
	 * 网络连接使用代理：默认，优先判断用户是否用代理</br> false:使用直连方式连接网络</br>
	 * true:使用当前代理IP与PORT方式连接网
	 * 
	 */
	public static boolean isUseProxy 	= true;
	final private static int TIMEOUT_2G		= 50000;
	final private static int TIMEOUT_3G 	= 35000;
	final private static int TIMEOUT_4G_WIFI= 20000;
/*	各种网络环境估值速率表
GPRS       	2G(2.5) General Packet Radia Service 114kbps
EDGE       	2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
UMTS      	3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
CDMA     	2G 电信 Code Division Multiple Access 码分多址
EVDO_0   	3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
EVDO_A  	3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
1xRTT      	2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
HSDPA    	3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps 
HSUPA    	3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
HSPA      	3G (分HSDPA,HSUPA) High Speed Packet Access 
IDEN      	2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
EVDO_B 		3G EV-DO Rev.B 14.7Mbps 下行 3.5G
LTE        	4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G 
EHRPD  		3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
HSPAP  		3G HSPAP 比 HSDPA 快些
 * */	
	
	private static int connectionTimeout = TIMEOUT_4G_WIFI;
	private static int readTimeout = TIMEOUT_4G_WIFI;
	//发送tcp协议时的handle处理超时时间
	public static int httpRequestTimeout = TIMEOUT_4G_WIFI;
	
	final public static short RETRY_TIME = 3;



    public static final String NETWORK_CLASS_WIFI = "ACCESS_TYPE_WIFI";
    
    public static final String NETWORK_CLASS_UNKNOWN = "ACCESS_TYPE_OTHER";
    /** Class of broadly defined "2G" networks. {@hide} */
    public static final String NETWORK_CLASS_2G = "ACCESS_TYPE_2G";
    /** Class of broadly defined "3G" networks. {@hide} */
    public static final String NETWORK_CLASS_3G = "ACCESS_TYPE_3G";
    /** Class of broadly defined "4G" networks. {@hide} */
    public static final String NETWORK_CLASS_4G = "ACCESS_TYPE_3G";
    
//	static {
//		setNetworkEvnParameter();
//	}

	public static String intToIp(int ipAddress) {
		//return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"." + ((i >> 24 ) & 0xFF);
		String ipString = null;
		if (ipAddress != 0) {
		       ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." 
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
		}
		return ipString;
	}
	public static String getIPAddress() {
		String IP = null;
		//StringBuilder IPStringBuilder = new StringBuilder();
		try {
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration
						.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface
						.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						// IPStringBuilder.append(inetAddress.getHostAddress().toString());
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {

		}
		//IP = IPStringBuilder.toString();
		return IP;
	}
	//根据不同网络环境做出不出的超时时间设置
	public static void setNetworkEvnParameter(Context context) {
		String type = getNetworkClass(context);
		if (type.equals(NETWORK_CLASS_2G)) {
			connectionTimeout = TIMEOUT_2G;
			readTimeout = TIMEOUT_2G;
			httpRequestTimeout = TIMEOUT_2G;
		} else if (type.equals(NETWORK_CLASS_3G)) {
			connectionTimeout = TIMEOUT_3G;
			readTimeout = TIMEOUT_3G;
			httpRequestTimeout = TIMEOUT_3G;
		} else if (type.equals(NETWORK_CLASS_4G)) {
			
		}
	}
    
    /**
     * Return general class of network type, such as "3G" or "4G". In cases
     * where classification is contentious, this method is conservative.
     */
    private static String getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
	
    /**
	 * 获取网络环境
	 * 
	 * @return
	 */
	public static String getNetworkClass(Context context) {
		NetType netType = getNetType(context);
		if (netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return NETWORK_CLASS_WIFI;
		}
		return getNetworkClass(netType.getNetworkType());
	}
    
	public static NetType getNetType(Context context) {

		NetType result = new NetType();// 默认为空内容

		ConnectivityManager connectivityManager = null;
		try {
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
		}

		if (null == connectivityManager) {
			return result;
		}

		if (!isNetworkAvailable(connectivityManager)) {
			return result;
		}

		int summaryType = getSummaryType(connectivityManager);

		NetworkInfo networkInfo = null;
		try {
			networkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Throwable e) {
		}

		String extraInfo = getExtraInfo(networkInfo);

		result = new NetType(context,summaryType, extraInfo);

		return result;
	}

	/**
	 * 判断是“手机网络”还是“无线网络”
	 */
	public static int getSummaryType(ConnectivityManager connectivityManager) {

		int result = NetType.SUMMARY_TYPE_OTHER;

		// mobile
		State mobile = null;
		try {
			mobile = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
		} catch (Throwable e) {
		}
		// wifi
		State wifi = null;
		try {
			wifi = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState();
		} catch (Throwable e) {
		}

		if (mobile == State.CONNECTED
				|| mobile == State.CONNECTING) {
			// mobile
			result = NetType.SUMMARY_TYPE_MOBILE;
		} else if (wifi == State.CONNECTED
				|| wifi == State.CONNECTING) {
			// wifi
			result = NetType.SUMMARY_TYPE_WIFI;
		}

		return result;
	}

	/**
	 * 网络额外信息
	 */
	public static String getExtraInfo(NetworkInfo networkInfo) {
		String result = "";
		try {
			result = networkInfo.getExtraInfo();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断是否有网络（遍历NetworkInfo）
	 */

	/**
	 * 检测网络连接是否可用
	 * 
	 * @param context
	 * @return true 可用; false 不可用
	 */
	final public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			return isNetworkAvailable(connectivity);
		}
		return false;
	}
	
	private static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
		NetworkInfo[] networkInfos = null;
		try {
			networkInfos = connectivityManager.getAllNetworkInfo();
			final int length = networkInfos.length;
			for (int i = 0; i < length; i++) {
				if (networkInfos[i].isConnected()) {
					return true;
				}
			}
		} catch (Throwable e) { }
		return false;
	}

	public static class NetType {

		public static final int NSP_CHINA_MOBILE = 1;// 移动
		public static final int NSP_CHINA_UNICOM = 2;// 联通
		public static final int NSP_CHINA_TELECOM = 3;// 电信
		public static final int NSP_OTHER = 0;// 其它
		public static final int NSP_NO = -1;// 不可用

		public static final int SUMMARY_TYPE_WIFI = 1;// WIFI
		public static final int SUMMARY_TYPE_MOBILE = 2;// MOBILE
		public static final int SUMMARY_TYPE_OTHER = 0;// 其它

		public String extraInfo;

		public int summaryType = SUMMARY_TYPE_OTHER;
		public String detailType;

		public Integer simState;
		public int networkType;
		public String networkTypeName;
		public String networkOperator;
		public String networkOperatorName;

		public String proxyHost;
		public Integer proxyPort;

		public NetType(Context context,int summaryType, String extraInfo) {
			this.summaryType = summaryType;
			this.extraInfo = extraInfo;
			getSimAndOperatorInfo(context);
		}

		public NetType() {
		}
		
		private void getSimAndOperatorInfo(Context context) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				simState = telephonyManager.getSimState();
			} catch (Throwable e) {
			}
			try {
				networkOperatorName = telephonyManager.getNetworkOperatorName();
			} catch (Throwable e) {
			}
			try {
				networkOperator = telephonyManager.getNetworkOperator();
			} catch (Throwable e) {
			}
			try {
				networkType = telephonyManager.getNetworkType();
				networkTypeName = getNetworkTypeName(networkType);
			} catch (Throwable e) {
			}

		}

		public int getNSP() {

			if (null == simState
					|| simState == TelephonyManager.SIM_STATE_UNKNOWN) {
				return NSP_NO;
			}
			if (TextUtils.isEmpty(networkOperatorName)
					&& TextUtils.isEmpty(networkOperator)) {
				return NSP_NO;
			}

			if (("中国移动".equalsIgnoreCase(networkOperatorName))//
					|| ("CMCC".equalsIgnoreCase(networkOperatorName)) //
					|| ("46000".equalsIgnoreCase(networkOperator))//
					|| ("China Mobile".equalsIgnoreCase(networkOperatorName))) {
				// 中国移动
				return NSP_CHINA_MOBILE;
			}
			if (("中国电信".equalsIgnoreCase(networkOperatorName))//
					|| ("China Telecom".equalsIgnoreCase(networkOperatorName))//
					|| ("46003".equalsIgnoreCase(networkOperator))) {
				// 中国电信
				return NSP_CHINA_TELECOM;
			}
			if (("中国联通".equalsIgnoreCase(networkOperatorName))//
					|| ("China Unicom".equalsIgnoreCase(networkOperatorName))//
					|| ("46001".equalsIgnoreCase(networkOperator))//
					|| ("CU-GSM".equalsIgnoreCase(networkOperatorName))) {
				// 中国联通
				return NSP_CHINA_UNICOM;
			}

			return NSP_OTHER;
		}

		public String getNetworkTypeName(int code) {
			switch (code) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "CDMA - EvDo rev. 0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "CDMA - EvDo rev. A";
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "CDMA - 1xRTT";
			default:
				return "UNKNOWN";
			}
		}

		public int getNetworkType() {
			return networkType;
		}

		public String getProxyHost() {
			String proxyHost = android.net.Proxy.getDefaultHost();

			if (SUMMARY_TYPE_WIFI == summaryType) {// 魅族切换到WIFI时proxyHost仍然按切换前返回，导致需要如此处理。
				return null;
			} else {
				this.proxyHost = proxyHost;
				this.proxyPort = android.net.Proxy.getDefaultPort();
			}

			return this.proxyHost;
		}

		public Integer getProxyPort() {
			return proxyPort;
		}

	}

	public static boolean is2GNetwork(Context context) {

		NetType netType = getNetType(context);
		if (netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return false;
		}

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephonyManager.getNetworkType();
		if (TelephonyManager.NETWORK_TYPE_CDMA == type
				|| TelephonyManager.NETWORK_TYPE_GPRS == type
				|| TelephonyManager.NETWORK_TYPE_EDGE == type) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是wifi环境
	 * 
	 * @return
	 */
	public static boolean isWifi(Context context) {
		NetType netType = getNetType(context);
		if (netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取网络链接，全局统一 2G网络需要走代理
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection getConncetion(Context context,String url) {
		HttpURLConnection conn = null;
		if (TextUtils.isEmpty(url))
			return conn;
		try {
			String proxyHost = null;
			Integer proxyPort = null;
			if (isUseProxy) {
				NetType netType = getNetType(context);
				proxyHost = netType.getProxyHost();
				proxyPort = netType.getProxyPort();
			}
			if (isUseProxy && null != proxyHost && null != proxyPort) {// 代理方式
				conn = (HttpURLConnection) new URL(url)
						.openConnection(new java.net.Proxy(
								java.net.Proxy.Type.HTTP,
								new InetSocketAddress(proxyHost, proxyPort)));
				
				//final Proxy proxy = new Proxy(Type.HTTP,InetSocketAddress.createUnresolved(proxyHost, proxyPort));
				//conn = (HttpURLConnection) new URL(url).openConnection(proxy);
				
			} else {// 直连方式
				isUseProxy = false;
				conn = (HttpURLConnection) new URL(url).openConnection();
			}
			conn.setConnectTimeout(connectionTimeout);
			conn.setReadTimeout(readTimeout);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			conn = null;
		} catch (IOException e) {
			e.printStackTrace();
			conn = null;
		}
		return conn;		
	}

}
