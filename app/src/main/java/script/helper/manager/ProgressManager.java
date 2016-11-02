package script.helper.manager;


import java.util.ArrayList;

import script.helper.utils.CMDUtils;
import script.helper.utils.ShellUtils;


public class ProgressManager {
	/**
	 * 用于管理进程
	 */

	public static void stopProgress(String progressName) {
		ShellUtils.CommandResult result = ShellUtils.execCommand("ps | grep "
				+ progressName);
		ArrayList<String> list = new ArrayList<String>();
		String pid = "";

		if (null != result.successMsg && !result.successMsg.trim().equals("")) {
			String[] xxStrings = result.successMsg.split(" ");
			for (int i = 0; i < xxStrings.length; i++) {
				if (xxStrings[i].trim().length() > 0) {
					list.add(xxStrings[i]);
				}
			}
			pid = list.get(1);
			CMDUtils.runCMD("kill " + pid, true, false);
		}

	}



	/**
	 * 启动特定的界面
	 * @param pkgName 包名
	 * @param actName 要启动Activity的名字
	 */
	public static void launchApp(String pkgName, String actName) {
		String commandString = "am start -n " + pkgName + "/" + actName;
		CMDUtils.runCMD(commandString, false, false);
	}
}
