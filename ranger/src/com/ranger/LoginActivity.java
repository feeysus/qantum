package com.ranger;

import java.util.List;

 
import com.google.gson.Gson;
import com.ranger.bean.Constant;
import com.ranger.bean.User;
import com.ranger.utils.LoadInfoHelp;
import com.ranger.utils.UpdateClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LoginActivity extends Activity {

	Context context;
	EditText edit_name;
	EditText edit_pwd;
	Gson gson = new Gson();
	SharedPreferences sp;
	CheckBox checkBoxlogin;
	CheckBox checkBoxPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context = this;

		edit_name = (EditText) findViewById(R.id.login_name);
		edit_pwd = (EditText) findViewById(R.id.login_password);
		
		checkBoxlogin = (CheckBox) findViewById(R.id.checkBoxlogin);
		checkBoxPass = (CheckBox) findViewById(R.id.checkBoxPass);

		edit_pwd.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode
						&& event.getAction() == KeyEvent.ACTION_DOWN) {

					if (Constant.UNNET_UNDATA.equals(getApp()
							.getSys_state())) {
						LoadInfoHelp.errorTips();
					} else
						loginButton(null);
					return true;
				}
				return false;
			}
		});

		sp = getPreferences(Activity.MODE_PRIVATE);
		getApp().setSp(sp);
		boolean login = sp.getBoolean("login", false);
		boolean pass = sp.getBoolean("pass", false);

		if (pass) {
			edit_name.setText(sp.getString("name", null));
			edit_pwd.setText(sp.getString("pwd", null));
		}
		if (login) {
			checkBoxlogin.setChecked(login);
			if (Constant.UNNET_UNDATA.equals(getApp()
					.getSys_state())) {
				LoadInfoHelp.errorTips();
			} else
				loginButton(null);
		}
		
		new UpdateClient(this);
	}

	public void loginButton(View view) {
		if ("".equals(edit_name.getText().toString())) {
			LoadInfoHelp.nullName();
			return;
		}
		if ("".equals(edit_pwd.getText().toString())) {
			LoadInfoHelp.nullPwd(context);
			return;
		}
		LoadInfoHelp.showProgress(context,true);
		
//		if (Constant.UNNET_DATA.equals(getApp().getSys_state())) {
//			new LoadDataTask().execute("local");
//		} else {
//			new LoadDataTask().execute("remote");
//		}
	}

	class LoadDataTask extends AsyncTask<String, Integer, String> {
		List<?> _list;

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
//			if ("local".equals(params[0]))
//				_list = DatabaseHelper.queryUser(edit_name.getText().toString()
//						.trim(), edit_pwd.getText().toString().trim());
//			else
//				_list = HttpUtil.getData(Constant.queryUser + "?name="
//						+ edit_name.getText().toString().trim() + "&pwd="
//						+ edit_pwd.getText().toString().trim());
			return "";
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (_list.size() == 0) {
				LoadInfoHelp.noUserTips();
			} else {
				User user = gson.fromJson(_list.get(0).toString(), User.class);
				//getApp().setUser(user);
				//DatabaseHelper.removeUserByID(user.getId());
				//DatabaseHelper.saveUser(_list);
				context.startActivity(new Intent(context, MainActivity.class));
				LoginActivity.this.finish();
			}
			LoadInfoHelp.hideProgress();
		}
	}

	@Override
	protected void onDestroy() {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("name", String.valueOf(edit_name.getText()));
		editor.putString("pwd", String.valueOf(edit_pwd.getText()));
		editor.putBoolean("login", checkBoxlogin.isChecked());
		editor.putBoolean("pass", checkBoxPass.isChecked());
		editor.commit();
		super.onDestroy();
	}

	public ApplicationHelper getApp() {
		return ((ApplicationHelper) super.getApplicationContext());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			System.exit(0);
		}
		return true;
	}
}
