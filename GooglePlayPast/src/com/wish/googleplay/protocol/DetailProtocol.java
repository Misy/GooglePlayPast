package com.wish.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wish.googleplay.domain.AppInfo;

public class DetailProtocol extends BaseProtocol<AppInfo> {

	private String packageName;

	public DetailProtocol(String packageName) {
		super();
		this.packageName = packageName;
	}

	@Override
	public AppInfo paserJson(String json) {
		List<String> screen;
		List<String> safeUrl;
		List<String> safeDesUrl;
		List<String> safeDes;
		List<Integer> safeDesColor;
		try {
			JSONObject object = new JSONObject(json);
			long id = object.getLong("id");
			String name = object.getString("name");
			String packageName = object.getString("packageName");
			String iconUrl = object.getString("iconUrl");
			float stars = Float.parseFloat(object.getString("stars"));
			long size = object.getLong("size");
			String downloadUrl = object.getString("downloadUrl");
			String des = object.getString("des");
			String downloadNum = object.getString("downloadNum");
			String version = object.getString("version");
			String date = object.getString("date");
			String author = object.getString("author");
			screen = new ArrayList<String>();
			JSONArray screenArray = object.getJSONArray("screen");
			for (int i = 0; i < screenArray.length(); i++) {
				screen.add(screenArray.getString(i));
			}
			safeUrl = new ArrayList<String>();
			safeDesUrl = new ArrayList<String>();
			safeDes = new ArrayList<String>();
			safeDesColor = new ArrayList<Integer>();
			JSONArray safeArray = object.getJSONArray("safe");
			for (int i = 0; i < safeArray.length(); i++) {
				JSONObject safeObject = safeArray.getJSONObject(i);
				safeUrl.add(safeObject.getString("safeUrl"));
				safeDesUrl.add(safeObject.getString("safeDesUrl"));
				safeDes.add(safeObject.getString("safeDes"));
				safeDesColor.add(safeObject.getInt("safeDesColor"));
			}
			AppInfo appInfo = new AppInfo(id, name, packageName, iconUrl,
					stars, size, downloadUrl, des, downloadNum, version, date,
					author, screen, safeUrl, safeDesUrl, safeDes, safeDesColor);
			return appInfo;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	protected String getParams() {
		return "&packageName=" + packageName;
	}
}
