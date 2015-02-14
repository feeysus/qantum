package com.ranger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
 
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.ranger.utils.TianDiLayer;
 

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class ArcMap {
	private Context context = null;
	private MapView mMapView = null;
 
	private GraphicsLayer graphicsLayer;
	private int graphics_id;
	private int graphic_add_id;

	private Graphic editGraphic;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private int state = -1;// 0为选中1 为增加小班 2 为修改 3为删除
	private Handler handler = null;

	private String filter_name = "XING_Z_QH";
	private String filter_value = "";
	public int sde_id = 0;

	public String current_featurelayer_name;
	public String current_sde_name;

	 
	public String where = "";
	private MapTouchListener mapTouchListener;
	private LongPressListener longPressListener;
 

	private List<int[]> list_shp = new ArrayList<int[]>();

	public List<int[]> getList_shp() {
		return list_shp;
	}

	public void setList_shp(List<int[]> list_shp) {
		this.list_shp = list_shp;
	}

	PictureMarkerSymbol markerSymbol;

	public ArcMap(Context mContext, MapView mapView, Handler handler) {
		this.context = mContext;
		this.mMapView = mapView;
		this.handler = handler;
		ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");

		mMapView.setEsriLogoVisible(false);
		mMapView.enableWrapAround(true);
		mMapView.setMapBackground(0xffe6e9ee, Color.TRANSPARENT, 0, 0);

		Layer layer =  new TianDiLayer(TianDiLayer.MapType.IMG, new UserCredentials(), false,
				true);
	    mMapView.addLayer(layer);
 
		mapTouchListener = new MapTouchListener(context, mMapView);
		longPressListener = new LongPressListener();
		mMapView.setOnTouchListener(mapTouchListener);
		mMapView.setOnLongPressListener(longPressListener);
	}

	 
	class LongPressListener implements OnLongPressListener {
		private static final long serialVersionUID = 1L;
		private int g_id = -1;

		@Override
		public boolean onLongPress(float x, float y) {

			Point point = mMapView.toMapPoint(x, y);
 
			// 转为80坐标系
			Point point_80 = (Point) GeometryEngine.project(point,
					mMapView.getSpatialReference(),
					SpatialReference.create(102100));

	 
			Graphic graphic = new Graphic(point, markerSymbol);
			if (g_id != -1)
				graphicsLayer.removeGraphic(g_id);
			g_id = graphicsLayer.addGraphic(graphic);

			return false;
		}

		public void removeMarker() {
			if (g_id != -1)
				graphicsLayer.removeGraphic(g_id);
		}

	}

	class MapTouchListener extends MapOnTouchListener {

		public MapTouchListener(Context context, MapView view) {
			super(context, view);
		}

		@Override
		public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {

			return super.onDragPointerMove(from, to);
		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, final MotionEvent to) {

			return super.onDragPointerUp(from, to);
		}

		@Override
		public boolean onSingleTap(final MotionEvent e) {
			 
			return true;
		}
	}

	 
	public MapView getmMapView() {
		return mMapView;
	}

	public ApplicationHelper getApplicationContext() {
		return ((ApplicationHelper) context.getApplicationContext());
	}

	public MapTouchListener getMapTouchListener() {
		return mapTouchListener;
	}
}
