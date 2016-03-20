package com.wish.googleplay.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.wish.googleplay.domain.UserInfo;

public class UserProtocol extends BaseProtocol<UserInfo> {

	@Override
	public UserInfo paserJson(String json) {
		UserInfo userInfo;
		// "{name:'Wish',email:'wishgelivable@gmail.com',url:'image/user.png'}"
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			String name = jsonObj.getString("name");
			String url = jsonObj.getString("url");
			String email = jsonObj.getString("email");
			userInfo = new UserInfo(name, url, email);
			return userInfo;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getKey() {
		return "user";
	}

}
