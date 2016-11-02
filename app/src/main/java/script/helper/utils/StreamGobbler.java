package script.helper.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/** 
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流 
 * 用于录屏，如果脚本执行错误则保留录像
 * @author cxq 
 * 
 */

public class StreamGobbler extends Thread {
	InputStream is;
	String type;
	OutputStream os;
	boolean isFailure = false;



	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;

	}

	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			if (os != null)
				pw = new PrintWriter(os);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) {

				if (pw != null)
					pw.println(line);
				if (type.equals("Output") && line.contains("FAILURES!!!")) {
					isFailure = true;
				}

			}
			// 流读取完毕之后，关闭线程
			if (type.equals("Out")) {
				if (isFailure) {
					// 如果出错，则保留最进一个MP4文件
					Log.i("ShellService", "脚本运行出错");
				} else {
					// 如果运行成功，则清空所有MP4
					Log.i("ShellService", "脚本运行完成");
				}
			}

			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {

			if (pw != null) {
				pw.close();
			}

			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
