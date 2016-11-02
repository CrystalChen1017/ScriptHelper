package script.helper.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * ShellUtils
 * <ul>
 * <strong>Check root</strong>
 * <li>{@link ShellUtils#checkRootPermission()}</li>
 * </ul>
 * <ul>
 * <strong>Execte command</strong>
 * <li>{@link ShellUtils#execCommand(String, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean, boolean)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
 */
public class ShellUtils {

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    /**
     * check whether has root permission
     *
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     *
     * @param command command
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, false,
                true);
    }

    /**
     * execute shell command, default return result msg
     *
     * @param command command
     * @param isRoot  whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command list
     * @param isRoot   whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands,
                                            boolean isRoot) {
        return execCommand(
                commands == null ? null : commands.toArray(new String[]{}),
                isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command array
     * @param isRoot   whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {

        return execCommand(commands, isRoot, true);
    }

    public static CommandResult execCommand(String[] commands) {

        return execCommand(commands, false, true);
    }

    /**
     * execute shell command
     *
     * @param command         command
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot,
                                            boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands        command list
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands,
                                            boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(
                commands == null ? null : commands.toArray(new String[]{}),
                isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands        command array
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     * <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and {@link CommandResult#errorMsg} is
     * null.</li>
     * <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
     * </ul>
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot,
                                            boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {

            process = Runtime.getRuntime().exec(
                    isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // 1、解决进程阻塞的问题，开启另一个线程，清空Process的缓存，缓存满了则会造成进程阻塞
                // 2、保留脚本运行出错时的录屏文件
                // 当是uiautomator,进入清空（含录屏）操作
                if (command.contains("instrument")) {// 只有执行脚本的时候需要清除缓存
                    StreamGobbler ssgError = new StreamGobbler(
                            process.getErrorStream(), "Error");
                    StreamGobbler ssgOutput = new StreamGobbler(
                            process.getInputStream(), "Output");
                    ssgError.start();
                    ssgOutput.start();
                }
                if (!command.contains("ps | grep monkey")) {
                    Log.i("ShellUtils.execCommand", "------------执行命令："
                            + command + "------------");
                }

                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();

            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();

            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(
                        process.getInputStream()), 1024);
                errorResult = new BufferedReader(new InputStreamReader(
                        process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                    // TestReport.s(s);
                }

                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                    // 屏蔽掉录像命令产生的反馈信息
                    if (!s.equals("The max width/height supported by codec is 1920x1088")) {
                        Log.e("ShellUtils", s);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();

            }
        }

        return new CommandResult(result, successMsg == null ? null
                : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CommandResult {

        /**
         * result of command
         **/
        public int result;
        /**
         * success message of command result
         **/
        public String successMsg;
        /**
         * error message of command result
         **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    /**
     * 执行一个数组命令
     *
     * @param commands      数组命令
     * @param oneByOne_flag true则按数组元素的先后顺序执行，false则批量处理
     * @author cxq
     */
    public static void executeCommandArray(String[] commands,
                                           boolean oneByOne_flag) {
        String TAG = "ShellService";
        if (null != commands) {

            if (oneByOne_flag) {
                for (int i = 0; i < commands.length; i++) {

                    CommandResult result = ShellUtils.execCommand(commands[i]);

                }
            } else {
                CommandResult result = ShellUtils.execCommand(commands);
            }
        }
    }
}
