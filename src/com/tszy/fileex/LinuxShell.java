package com.tszy.fileex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

/**
 * 底层linux命令工具
 * 
 * @author JianbinZhu
 * 
 */
public class LinuxShell {
	public static final String apkRoot = "chmod 777 ";

	/**
	 * 获取ROOT权限
	 * 
	 * @param r
	 * @param wait
	 * @return 成功返回true，失败返回false
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean getRoot(Runtime r, long wait) throws IOException, InterruptedException {
		boolean root = false;
		Process p = null;
		BufferedReader errReader = null;

		p = Runtime.getRuntime().exec("su");
		Thread.sleep(wait);

		errReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		if (!errReader.ready()) {
			root = true;
			p.destroy();
		}

		return root;
	}

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * 
	 * @param command
	 *            命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
	 * @return 应用程序是/否获取Root权限
	 */
	public static boolean getRoot(Context context) {
		Process process = null;
		DataOutputStream os = null;

		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(apkRoot + context.getPackageCodePath() + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT FAL" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		Log.d("*** DEBUG ***", "Root SUC ");
		return true;
	}
}
