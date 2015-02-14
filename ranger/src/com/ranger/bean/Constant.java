package com.ranger.bean;

import com.ranger.R;

import android.content.Context;


public class Constant {
    public static String url_server = "";
	public static String queryUser = "";
	public static String queryBase = "";
	public static String queryDic = "";
	public static String queryLayer = "";
	public static String upload = "";
	public static String queryVersion = "";
	public static String queryFile = "";
	public static String deleteFile = "";

	public static String NET_DATA = "NET_DATA";// 有网络有数据
	public static String NET_UNDATA = "NET_UNDATA";// 有网络无数据
	public static String UNNET_DATA = "UNNET_DATA";// 无网络有数据
	public static String UNNET_UNDATA = "UNNET_UNDATA";// 无网络无数据

	public static String ASYNC_DATA = "ASYNC_DATA";

	public Constant(Context context) {
		url_server = context.getResources().getString(
				R.string.url_server);

		queryBase = url_server + "queryBase.do";
		queryDic = url_server + "queryDicData.do";
		queryLayer = url_server + "queryLayer.do";
		queryUser = url_server + "queryUsers.do";
		upload = url_server + "upload.do";
		queryVersion = url_server + "queryVersion.do";
		queryFile = url_server + "queryFile.do";
		deleteFile = url_server + "deleteFile.do";
	}
}
