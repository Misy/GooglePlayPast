package com.wish.googleplay.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

import android.os.SystemClock;

import com.lidroid.xutils.util.IOUtils;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.http.HttpHelper.HttpResult;
import com.wish.googleplay.tools.FileUtils;

public abstract class BaseProtocol<T> {
	public T load(int index) {
		SystemClock.sleep(1000);
		String json = loadLocal(index);
		if (json == null) {
			json = loadServer(index);
			if (json != null) {
				saveLocal(json, index);
			}
		}
		if (json != null) {
			return paserJson(json);
		} else {
			return null;
		}
	}

	protected String getParams() {
		return "";
	}

	private String loadServer(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
				+ "?index=" + index + getParams());
		String json = httpResult.getString();
		return json;
	}

	private void saveLocal(String json, int index) {

		BufferedWriter bw = null;
		try {
			File dir = FileUtils.getCacheDir();
			File file = new File(dir, getKey() + "_" + index+getParams()); // /mnt/sdcard/googlePlay/cache/home_0
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(System.currentTimeMillis() + 1000 * 100 + "");
			bw.newLine();// ����
			bw.write(json);// ������json�ļ���������
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bw);
		}
	}

	private String loadLocal(int index) {
		File dir = FileUtils.getCacheDir();// ��ȡ�������ڵ��ļ���
		File file = new File(dir, getKey() + "_" + index+getParams());
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			long outOfDate = Long.parseLong(br.readLine());
			if (System.currentTimeMillis() > outOfDate) {
				return null;
			} else {
				String str = null;
				StringWriter sw = new StringWriter();
				while ((str = br.readLine()) != null) {

					sw.write(str);
				}
				return sw.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����json
	 * 
	 * @param json
	 * @return
	 */
	public abstract T paserJson(String json);

	/**
	 * ˵���˹ؼ���
	 * 
	 * @return
	 */
	public abstract String getKey();
}
