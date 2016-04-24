package com.fju.zqc.fjuzqcgradutation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Caiyuan Huang
 * <p>
 * 2014-11-3下午3:46:20
 * </p>
 * <p>
 * SharedPreference数据存储工具类
 * </p>
 */
class SharedPreferenceUtils {
	public SharedPreferences.Editor mEditor;
	public SharedPreferences mSharedPreferences;

	public SharedPreferenceUtils() {
		mSharedPreferences = AppUtils.getAppContext()
				.getSharedPreferences(
						AppUtils.getAppContext().getPackageName(),
						Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void saveBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value).commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public void saveInt(String key, int value) {
		mEditor.putInt(key, value).commit();
	}

	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	public void saveFloat(String key, float value) {
		mEditor.putFloat(key, value).commit();
	}

	public float getFloat(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	public void saveString(String key, String value) {
		mEditor.putString(key, value).commit();
	}

	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public void saveLong(String key, long value) {
		mEditor.putLong(key, value).commit();
	}

	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	/**
	 * 保存对象，object所在的类必须实现{@link Serializable}接口
	 * 
	 * @param key
	 * @param object
	 */
	public void saveObject(String key, Object object) {
		if ((object instanceof Serializable) == false) {
			throw new RuntimeException("请将object所在的类实现Serializable接口");
		}
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			String strBase64 = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			saveString(key, strBase64);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取存储的object
	 * 
	 * @param key
	 * @param defValue
	 * @return 获取成功则返回存储的object，否则返回默认值
	 */
	public Object getObject(String key, Object defValue) {
		Object object = null;
		String strBase64 = getString(key, "");
		if (TextUtils.isEmpty(strBase64)) {
			return defValue;
		}
		byte[] base64 = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais = null;
		ObjectInputStream bis = null;
		try {
			bais = new ByteArrayInputStream(base64);
			bis = new ObjectInputStream(bais);
			object = bis.readObject();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				bais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return defValue;
	}

}
