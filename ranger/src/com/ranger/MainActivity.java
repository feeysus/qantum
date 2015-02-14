package com.ranger;


import com.esri.android.map.MapView;
import com.ranger.utils.Blur;

import android.app.Activity; 
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity{
	 
	MapView mMapView;
	ArcMap arcmap;
	LinearLayout buttons;
	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
	     setContentView(R.layout.main);
	 
		mMapView = (MapView) findViewById(R.id.map);
		buttons = (LinearLayout) findViewById(R.id.buttons);
		arcmap =  new ArcMap(this, mMapView, null);
		
		imageView = (ImageView)findViewById(R.id.blurredOverlay);
		Bitmap actionBarSection = Bitmap.createBitmap(300, 200, Bitmap.Config.ARGB_8888);
		actionBarSection = Blur.apply(this, actionBarSection);
		imageView.setImageBitmap(actionBarSection);
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the enu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setButton(View v){
		
	}
	
	public void star_go(View v){
		buttons.setVisibility(View.GONE);
		mMapView.setEnabled(true);
	}
}
