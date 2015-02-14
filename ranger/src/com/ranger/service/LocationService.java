
package com.ranger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.android.map.GraphicsLayer; 
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.ranger.ApplicationHelper;
import com.ranger.ArcMap;
import com.ranger.R;
import com.ranger.db.DatabaseHelper;
 

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public final class LocationService extends Service {

	private static final String SERVICE_NAME = "com.gis.service.LocationService";

	static Context mcontext;
	private LocationClient mLocClient;
	private static ArcMap arcMap;
	private static String mode = "none";
	private GraphicsLayer recordLayer = null;
	private GraphicsLayer locationLayer = null;
	private ArrayList<Map<String, String>> _list = new ArrayList<Map<String, String>>();
	public static String route_title = "";
	PictureMarkerSymbol markerSymbol;

	public LocationService() {

	}

	public ApplicationHelper getApplicationContext() {
		return ((ApplicationHelper) mcontext.getApplicationContext());
	}

	@Override
	public void onCreate() {
		initLocation();
	}

	@Override
	public void onDestroy() {
		mLocClient.stop();
		if ("record".equals(mode)) {
			//DatabaseHelper.insertrRoute(route_title, _list);
			_list.clear();
		}

		if (locationLayer != null)
			arcMap.getmMapView().removeLayer(locationLayer);
		if (recordLayer != null)
			arcMap.getmMapView().removeLayer(recordLayer);
	}

	public static void startService(final Context context, final String m) {

		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = LocationService.getIntent();
				context.startService(intent);
				mcontext = context;
				mode = m;
			}
		});
		serviceThread.start();
	}

	public static void stopService(final Context context, final String m) {
		Intent intent = LocationService.getIntent();
		context.stopService(intent);
		mode = m;
	}

	public static Intent getIntent() {
		return new Intent(SERVICE_NAME);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initLocation() {
		if(mcontext==null)
			return;
//		markerSymbol = new PictureMarkerSymbol(mcontext.getResources()
//				.getDrawable(R.drawable.user_center_hot));
		arcMap = getApplicationContext().getArcmap();
		mLocClient = new LocationClient(getApplication());

		mLocClient.setAK("Y2t0wyTiKynusmor9n30MFvo");
		mLocClient.registerLocationListener(new MyLocationListenner());

		setLocationOption();
		mLocClient.start();
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(false);

		if ("record".equals(mode)) {
			option.setScanSpan(60000);
			recordLayer = new GraphicsLayer();
			arcMap.getmMapView().addLayer(recordLayer);
		} else {
			locationLayer = new GraphicsLayer();
			arcMap.getmMapView().addLayer(locationLayer);
			option.setPriority(LocationClientOption.NetWorkFirst);
		}

		option.disableCache(true);
		mLocClient.setLocOption(option);
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			if (location.getLocType() != 161) {
				mLocClient.requestLocation();
				return;
			}
			updateLocation(location);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}

	}

	private void updateLocation(BDLocation location) {
		if (location != null) {
			// 手动做的偏差,为了适应WGS84天地图
			double locx = location.getLongitude() - 0.0121;
			double locy = location.getLatitude() - 0.0043;
			Point mapPoint = new Point();
			mapPoint.setX(locx);
			mapPoint.setY(locy);

			markLocation(mapPoint);

		}
	}

	private void markLocation(Point wgspoint) {
		wgspoint.setX(wgspoint.getX());
		wgspoint.setY(wgspoint.getY());

		Graphic graphic = new Graphic(wgspoint, markerSymbol);
		if ("record".equals(mode)) {
			recordLayer.addGraphic(graphic);

			Map<String, String> map = new HashMap<String, String>();

			map.put("LAT", String.valueOf(wgspoint.getX()));
			map.put("LNG", String.valueOf(wgspoint.getY()));
			map.put("TM", String.valueOf(new Date().getTime()));
			_list.add(map);

		} else
			locationLayer.addGraphic(graphic);

		arcMap.getmMapView().setExtent(wgspoint);
	}
}
