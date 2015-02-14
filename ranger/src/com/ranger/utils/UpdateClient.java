package com.ranger.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ranger.bean.Constant;

 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class UpdateClient {

	public Context context;
	// 远程下载用到的变量
	  String currentFilePath = "";
	private String currentTempFilePath = "";
	private String strURL = "";
	  String fileEx = "";
	private String fileNa = "";
	String updateUrl;
	String newVersion;

	public UpdateClient(Context context) {
		this.context = context;
		new UpdateTask().execute();
	}

	public UpdateClient() {
	}

	// 检查更新类
	class UpdateTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			return checkUpdate() + "";
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			if ("true".equalsIgnoreCase(result)) {
				startUpdate(updateUrl);
			}
			super.onPostExecute(result);
		}
	}

	public boolean checkUpdate() {
		List<Object> _list = HttpUtil.getData(Constant.queryVersion);
		for (int i = 0; i < _list.size(); i++) {
			JSONObject json = null;
			try {
				json = new JSONObject(_list.get(i).toString());
				updateUrl = json.getString("url");
				newVersion = json.getString("version");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (getOldVersion() < Integer.parseInt(newVersion)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// 开始更新
	public void startUpdate(final String updateurl) {
		new AlertDialog.Builder(context)
				.setTitle("更新提示")
				.setMessage("发现新版本，是否更新")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						LoadInfoHelp.showProgress(context,true);
						strURL = updateurl;
						/* 取得欲安装程序之文件名称 */
						fileEx = strURL.substring(strURL.lastIndexOf(".") + 1,
								strURL.length()).toLowerCase();
						fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
								strURL.lastIndexOf("."));
						getFile(strURL);
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

	// 获取老版本
	public int getOldVersion() {
		int appVersion = 0;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);

			appVersion = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersion;
	}

	/* 处理下载URL文件自定义函数 */
	private void getFile(final String strPath) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					openFile(downLoadFile(strPath));
					LoadInfoHelp.hideProgress();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
	}

	protected File downLoadFile(String httpUrl) {

		String root_path = Environment.getExternalStorageDirectory()
				+ "/for_res/update/";

		File tmpFile = new File(root_path);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		final File file = new File(root_path + fileNa + ".apk");

		try {
			URL url = new URL(httpUrl);
			try {
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[256];
				conn.connect();
				double count = 0;
				if (conn.getResponseCode() >= 400) {
					Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
				} else {
					while (count <= 100) {
						if (is != null) {
							int numRead = is.read(buf);
							if (numRead <= 0) {
								break;
							} else {
								fos.write(buf, 0, numRead);
							}
						} else {
							break;
						}

					}
				}

				conn.disconnect();
				fos.close();
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

		return file;
	}

	// 打开APK程序代码

	private void openFile(File file) {
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/* 自定义删除文件方法 */
	private void delFile() {
		if (currentTempFilePath.equals("")) {
			return;
		}
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

}