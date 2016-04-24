package com.fju.zqc.fjuzqcgradutation.utils;

import android.text.TextUtils;


/**
 * Caiyuan Huang
 * <p>
 * 2015-7-13
 * </p>
 * <p>
 * 数据保存工具类，用于保存轻量级的用户数据
 * </p>
 */
public class DataStorageUtils {
	private static SharedPreferenceUtils sp = new SharedPreferenceUtils();

	/**
	 * 设置被搜索用户的pid
	 * 
	 * @param pid
	 */
	public static void setOtherPid(String pid) {
		sp.saveString("otherPid", pid);
	}

	/**
	 * 获取pid
	 * 
	 * @return
	 */
	public static String getOtherPid() {
		return sp.getString("otherPid", "");
	}

	/**
	 * 设置pid
	 * 
	 * @param pid
	 */
	public static void setPid(String pid) {
		sp.saveString("pid", pid);
	}

	/**
	 * 获取pid
	 * 
	 * @return
	 */
	public static String getPid() {
		return sp.getString("pid", "");
	}
	/**
	 * 设置当前用户的昵称
	 *
	 * @param nickname
	 */
	public static void setUserNickName(String nickname) {
		sp.saveString("NickName", nickname);
	}

	/**
	 * 获取当前用户的昵称
	 *
	 * @return
	 */
	public static String getUserNickName() {
		return sp.getString("NickName", "");
	}

    /**
     * 设置下次是否显示提示
     * @param isTrue
     */
    public static void setIsShowNotify(boolean isTrue){
        sp.saveBoolean("isTrue",isTrue);
    }

    /**
     * 下次是否显示提示
     * @return
     */
    public static boolean getIsShowNotify(){
        return sp.getBoolean("isTrue",false);
    }
	/**
	 * 设置当前用户的头像文件id
	 * 
	 * @param profileFid
	 */
	public static void setCurUserProfileFid(String profileFid) {
		sp.saveString("cur_user_profile_fid", profileFid);
	}

	/**
	 * 设置总积分
	 * 
	 * @param score
	 */
	public static void setTotalScore(String score) {
		sp.saveString("score", score);
	}

	public static String getTotalScore() {
		return sp.getString("score", "");
	}

	/**
	 * 获取当前用户的头像文件id
	 * 
	 * @return
	 */
	public static String getCurUserProfileFid() {
		return sp.getString("cur_user_profile_fid", "");
	}

	/**
	 * 设置图片上传地址
	 * 
	 * @param url
	 */
	public static void setUploadUrl(String url) {
		sp.saveString("upload_url", url);
	}

	/**
	 * 获取图片上传地址
	 * 
	 * @return
	 */
	public static String getUploadUrl() {
		return sp.getString("upload_url",
				"http://121.40.16.113:8081/cgi-bin/upload.pl");
	}

	/**
	 * 设置下载地址
	 * 
	 * @param url
	 */
	public static void setDownloadUrl(String url) {
		sp.saveString("download_url", url);
	}

	/**
	 * 获取文件下载地址
	 * 
	 * @return
	 */
	public static String getDownloadUrl() {
		return sp.getString("download_url",
				"http://121.40.16.113:8081/cgi-bin/download.pl");
	}

	/**
	 * 获取头像下载地址
	 * 
	 * @param fid
	 * @return
	 */
	public static void setIsLogIn(boolean isLogIn){
		sp.saveBoolean("isLogIn",isLogIn);
	}
	public static boolean getIsLogIn(){
		return sp.getBoolean("isLogIn",false);
	}
	public static String getHeadDownloadUrl(String fid) {
		if (TextUtils.isEmpty(fid))
			return "";
		if (fid.contains("http://"))
			return fid;
		return getDownloadUrl() + "?fid=" + fid;
	}

	public static String getSession() {
		return sp.getString("session", "");
	}

	public static void setSession(String session) {
		if (session == null) {
			session = "";
		}
		sp.saveString("session", session);
	}

}
