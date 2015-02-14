package com.ranger.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.map.Graphic;
import com.ranger.ApplicationHelper;
import com.ranger.bean.Constant;
 
 
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class AsyncService extends Service {

	private static final String SERVICE_NAME = "com.gis.service.AsyncService";

	static Context mcontext; 
	static String mode = "";
	boolean isConnect;

	public ApplicationHelper getApplicationContext() {
		return ((ApplicationHelper) mcontext.getApplicationContext());
	}

	@Override
	public void onCreate() {
		mcontext = this.getBaseContext();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//处理网络与数据问题
		if (getApplicationContext().isNetAvailable()) {
			if (Constant.ASYNC_DATA.equals(mode))
				asyncGEO();
		} else {
			if (checkSYSDB()) {
				setEnvironmentTag(Constant.UNNET_DATA);
			} else {
				setEnvironmentTag(Constant.UNNET_UNDATA);
			}
		}
	}

	@Override
	public void onDestroy() {
		 
	}

	public static void startService(final Context context, String way) {
		mode = way;
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = AsyncService.getIntent();
				context.startService(intent);
				mcontext = context;
			}
		});
		serviceThread.start();
	}

	public static void stopService(final Context context) {
		Intent intent = AsyncService.getIntent();
		context.stopService(intent);
	}

	public static Intent getIntent() {
		return new Intent(SERVICE_NAME);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class asyncTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			asyncGEO();
			return "";
		}

	}

	private void asyncGEO() {
		
	}

 
    private boolean checkSYSDB(){
    	 return true;
    }
	
	// 设置环境标识
	private void setEnvironmentTag(String tag) {
		mcontext.sendBroadcast(new Intent(tag));
		getApplicationContext().setSys_state(tag);
	}
}
