package com.ranger.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class LoadInfoHelp {
	public static ProgressDialog pd;
	private static Context context;

	public LoadInfoHelp(Context mcontext) {
		context = mcontext;
	}

	// 显示加载中
	public static void showProgress(Context context,boolean cancel) {
		pd = ProgressDialog.show(context, "", "正在处理,请稍等..", true, cancel);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	// 显示加载中
	public static void showProgress(Context context, String content) {
		pd = ProgressDialog.show(context, "", content, true, true);
	}

	// 取消显示加载中
	public static void hideProgress() {
		if (pd != null && pd.isShowing())
			pd.dismiss();
	}

	
	public static void areaTips(String area){
		Toast.makeText(context, "当前面积:"+ area, Toast.LENGTH_LONG).show();
	}
	
	// 错误提示
	public static void errorTips() {
		Toast.makeText(context, "初始化数据失败，请检查网络！", Toast.LENGTH_LONG).show();
	}

	// 错误提示
		public static void unNetTips() {
			Toast.makeText(context, "当前没有连接网络！", Toast.LENGTH_LONG).show();
		}
	
	// 无数据提示
	public static void nullTips() {
		Toast.makeText(context, "暂无数据!", Toast.LENGTH_LONG).show();
	}

	// 无数据提示
	public static void serachNullTips() {
		Toast.makeText(context, "没有搜索到数据!", Toast.LENGTH_LONG).show();
	}

	//
	public static void noUserTips() {
		Toast.makeText(context, "用户名或密码有误!", Toast.LENGTH_LONG).show();
	}

	public static void nullName() {
		Toast.makeText(context, "请输入用户名!", Toast.LENGTH_LONG).show();
	}

	public static void nullPwd(Context context) {
		Toast.makeText(context, "请输入密码!", Toast.LENGTH_LONG).show();
	}

	public static void loaction(Context context) {
		Toast.makeText(context, "正在定位...", Toast.LENGTH_LONG).show();
	}

	public static void record(Context context) {
		Toast.makeText(context, "开始记录路线", Toast.LENGTH_LONG).show();
	}

	public static void exitTip() {
		Toast.makeText(context, "再按一次退出", Toast.LENGTH_LONG).show();
	}

	public static void clearTip() {
		Toast.makeText(context, "清理完成", Toast.LENGTH_LONG).show();
	}

	public static void noUpdateTip() {
		Toast.makeText(context, "暂无更新", Toast.LENGTH_LONG).show();
	}

	public static void downloadTip() {
		Toast.makeText(context, "已在后台下载", Toast.LENGTH_LONG).show();
	}

	public static void downloadcompleteTip() {
		Toast.makeText(context, "下载已完成", Toast.LENGTH_LONG).show();
	}
	
	public static void downloadUnneedTip() {
		Toast.makeText(context, "当前已是最大级别，无需下载!", Toast.LENGTH_LONG).show();
	}
}
