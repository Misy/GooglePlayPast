package com.wish.googleplay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wish.googleplay.domain.AppInfo;

public class GameProtocol extends BaseProtocol<List<AppInfo>> {

	@Override
	public List<AppInfo> paserJson(String json) {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				long id = jsonObj.getLong("id");
				String name = jsonObj.getString("name");
				String packageName = jsonObj.getString("packageName");
				String iconUrl = jsonObj.getString("iconUrl");
				float stars = Float.parseFloat(jsonObj.getString("stars"));
				long size = jsonObj.getLong("size");
				String downloadUrl = jsonObj.getString("downloadUrl");
				String des = jsonObj.getString("des");
				AppInfo info = new AppInfo(id, name, packageName, iconUrl,
						stars, size, downloadUrl, des);
				appInfos.add(info);
			}
			return appInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getKey() {
		return "game";
	}

}
