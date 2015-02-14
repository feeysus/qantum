package com.ranger.utils;

import java.net.URL;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
 
import android.os.Environment;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import com.esri.core.io.UserCredentials;

public class TianDiLayer extends TiledServiceLayer {

	public enum MapType {
		VEC, IMG, CVA, CIA
	}

	public static String cachepath = "";
	private boolean isCache = false;
	public MapType mapType;
	private TileInfo tiandituTileInfo;
	private static final Point ORIGIN_2000 = new Point(-180, 90);
	
	private int minLevel = 0;
	private int maxLevel = 21;

	public int getMaxLevel() {
		return maxLevel;
	}

	double[] res = { 1.40625, 0.703125, 0.3515625, 0.17578125, 0.087890625,
			0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625,
			0.00274658203125, 0.001373291015625, 0.0006866455078125,
			0.00034332275390625, 0.000171661376953125, 8.58306884765629E-05,
			4.29153442382814E-05, 2.14576721191407E-05, 1.07288360595703E-05,
			5.36441802978515E-06, 2.68220901489258E-06, 1.34110450744629E-06 };
	double[] scale = { 400000000, 295497598.5708346, 147748799.285417,
			73874399.6427087, 36937199.8213544, 18468599.9106772,
			9234299.95533859, 4617149.97766929, 2308574.98883465,
			1154287.49441732, 577143.747208662, 288571.873604331,
			144285.936802165, 72142.9684010827, 36071.4842005414,
			18035.7421002707, 9017.87105013534, 4508.93552506767,
			2254.467762533835, 1127.2338812669175, 563.616940 };

	public TianDiLayer(MapType mapType, UserCredentials usercredentials,
			boolean isCache, boolean init) {
		super("");
		this.isCache = isCache;
		this.mapType = mapType;
		setCredentials(usercredentials);
		cachepath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/for_res/gmap/" + mapType.name();
		if (init)
			try {
				getServiceExecutor().submit(new Runnable() {

					public final void run() {
						a.initLayer();
					}

					final TianDiLayer a;

					{
						a = TianDiLayer.this;
						// super();
					}
				});
				return;
			} catch (RejectedExecutionException _ex) {

			}
	}

	private void buildTileInfo() {
		this.tiandituTileInfo = new com.esri.android.map.TiledServiceLayer.TileInfo(
				ORIGIN_2000, scale, res, 20, 96, 256, 256);
		this.setTileInfo(this.tiandituTileInfo);
	}

	public MapType getMapType() {
		return this.mapType;
	}

	public String getMapUrl(int _level, int _col, int _row) {
		
		if (_level > maxLevel || _level < minLevel) {
			return null;
		}
		
		StringBuilder url = new StringBuilder("http://t");
		Random random = new Random();
		int subdomain = (random.nextInt(6) + 1);
		url.append(subdomain);
		switch (mapType) {
		case VEC:
			url.append(".tianditu.com/DataServer?T=vec_c&X=").append(_col)
					.append("&Y=").append(_row).append("&L=").append(_level);
			break;
		case CVA:
			url.append(".tianditu.com/DataServer?T=cva_c&X=").append(_col)
					.append("&Y=").append(_row).append("&L=").append(_level);
			break;
		case CIA:
			url.append(".tianditu.com/DataServer?T=cia_c&X=").append(_col)
					.append("&Y=").append(_row).append("&L=").append(_level);
			break;
		case IMG:
			url.append(".tianditu.com/DataServer?T=img_c&X=").append(_col)
					.append("&Y=").append(_row).append("&L=").append(_level);
			break;
		default:
			return null;
		}
		return url.toString();
	}

	protected void initLayer() {
		this.buildTileInfo();
		this.setFullExtent(new Envelope(-180, -90, 180, 90));
		// this.setDefaultSpatialReference(SpatialReference.create(4490)); //
		// CGCS2000
		this.setInitialExtent(new Envelope(90.52, 33.76, 113.59, 42.88));
		this.setDefaultSpatialReference(SpatialReference.create(4326));
		super.initLayer();
	}

	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		byte[] iResult = null;

		iResult = getOfflineCacheFile(level, col, row);

		if (iResult == null) {
			String url = this.getMapUrl(level, col, row);
			try {
				URL iURL = null;
				byte[] iBuffer = new byte[1024];
				HttpURLConnection iHttpURLConnection = null;
				BufferedInputStream iBufferedInputStream = null;
				ByteArrayOutputStream iByteArrayOutputStream = null;

				iURL = new URL(url);
				iHttpURLConnection = (HttpURLConnection) iURL.openConnection();
				iHttpURLConnection.connect();
				iBufferedInputStream = new BufferedInputStream(
						iHttpURLConnection.getInputStream());
				iByteArrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int iLength = iBufferedInputStream.read(iBuffer);
					if (iLength > 0) {
						iByteArrayOutputStream.write(iBuffer, 0, iLength);
					} else {
						break;
					}
				}

				iBufferedInputStream.close();
				iHttpURLConnection.disconnect();

				iResult = iByteArrayOutputStream.toByteArray();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (isCache) {
				Map<String, String> map = null;
				byte[] bytes = com.esri.core.internal.io.handler.a.a(url, map);
				AddOfflineCacheFile(level, col, row, bytes);
			}
		}
		return iResult;
	}

	@Override
	public TileInfo getTileInfo() {
		return this.tiandituTileInfo;
	}

	//
	public byte[] AddOfflineCacheFile(int level, int col, int row, byte[] bytes) {

		File file = new File(cachepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File levelfile = new File(cachepath + "/" + level);
		if (!levelfile.exists()) {
			levelfile.mkdirs();
		}
		File colfile = new File(cachepath + "/" + level + "/" + col);
		if (!colfile.exists()) {
			colfile.mkdirs();
		}
		File rowfile = new File(cachepath + "/" + level + "/" + col + "/" + row
				+ ".png");
		if (!rowfile.exists()) {
			try {
				rowfile.createNewFile();
				 new FileOutputStream(rowfile).write(bytes);
			 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	private byte[] getOfflineCacheFile(int level, int col, int row) {
		byte[] bytes = null;
		File rowfile = new File(cachepath + "/" + level + "/" + col + "/" + row
				+ ".png");
		if (rowfile.exists()) {
			try {
				bytes = CopySdcardbytes(rowfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			bytes = null;
		}
		return bytes;
	}

	public byte[] CopySdcardbytes(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		byte[] bytes = out.toByteArray();
		return bytes;
	}

}
