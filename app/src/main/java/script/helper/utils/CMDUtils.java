package script.helper.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * execute cmd commands
 */
public class CMDUtils {

	public static class CMD_Result {
		public int resultCode;
		public String error;
		public String success;

		public CMD_Result(int resultCode, String error, String success) {
			this.resultCode = resultCode;
			this.error = error;
			this.success = success;
		}

	}

	/**
	 *
	* @param 	command command
	* @param	isShowCommand  is commnan content showed
	* @param	isNeedResultMsg is result needed
	* @retrun CMD_Result
	 */
	public static CMD_Result runCMD(String command, boolean isShowCommand,
			boolean isNeedResultMsg) {
		if (isShowCommand)
			Log.i("runCMD:" , command);
		CMD_Result cmdRsult = null;
		int result;
		try {
			Process process = Runtime.getRuntime().exec(command);
			result = process.waitFor();
			if (isNeedResultMsg) {
				StringBuilder successMsg = new StringBuilder();
				StringBuilder errorMsg = new StringBuilder();
				BufferedReader successResult = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				BufferedReader errorResult = new BufferedReader(
						new InputStreamReader(process.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
				cmdRsult = new CMD_Result(result, errorMsg.toString(),
						successMsg.toString());
			}
		} catch (Exception e) {
			Log.i("runCMD:" ,command + " failed");
			e.printStackTrace();
		}
		return cmdRsult;
	}

}
