package com.ranger;

import com.ranger.utils.FileUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

 

public class ApplicationHelper extends Application {

	public SharedPreferences getSp() {
		return sp;
	}

	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}

	private ArcMap arcmap;
	private boolean netAvailable = false;
	private Context context;
	private static ApplicationHelper mInstance = null;
 
	private String sys_state = "";
	private SharedPreferences sp = null;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		mInstance = this;
		getnetworkInfo();
 

		new FileUtils().creatSDDir("for_res/img");
		new FileUtils().creatSDDir("for_res/gmap");
		new FileUtils().creatSDFile("for_res/.nomedia");
	}

 

	private void getnetworkInfo() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			boolean isConnect = networkInfo.isConnected();
			setNetAvailable(isConnect);
		}
	}

	public static ApplicationHelper getInstance() {
		return mInstance;
	}

	public boolean isNetAvailable() {
		return netAvailable;
	}

	public void setNetAvailable(boolean netAvailable) {
		this.netAvailable = netAvailable;
	}

	public ArcMap getArcmap() {
		return arcmap;
	}

	public void setArcmap(ArcMap arcmap) {
		this.arcmap = arcmap;
	}

	public String getSys_state() {
		return sys_state;
	}

	public void setSys_state(String sys_state) {
		this.sys_state = sys_state;
	}
}
