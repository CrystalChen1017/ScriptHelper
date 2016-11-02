package script.helper.uiauto;

import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by CrystalChen on 2016/10/31.
 */

public class AccessibilityServiceUtil {
    final static String TAG = "AccessibilityUtil";

    /**
     * Check if accessibility service is on
     * @param context
     * @return true if service is on,else return false
     */
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    /**
     * Check if text is exist
     * @param text the text need to be checked
     * @param nodeInfo AccessibilityNodeInfo
     * @return true if text is exist, else return false
     */
    public static boolean searchText(String text, AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
            if (list != null && list.size() > 0)
                return true;
        }
        return false;

    }

    /**
     * Click on the view with specified text
     * <p><b>Note: </b>if there are many views satisfied this text, all views will be clicked!</p>
     */
    public static void clickOnText(String text, AccessibilityNodeInfo nodeInfo) throws Exception {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (accessibilityNodeInfos == null || accessibilityNodeInfos.size() < 1) {
            Exception e = new Exception("text = [" + text + "] not founded!");
            throw e;
        }

        for (int i = 0; i < accessibilityNodeInfos.size(); i++) {
            if (accessibilityNodeInfos.get(i).isEnabled()) {
                accessibilityNodeInfos.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                SystemClock.sleep(500);
            }
        }

    }
    /**
     * Click on the view with specified id
     * <p><b>Note: </b>if there are many views satisfied this id, all views will be clicked!</p>
     */
    public static void clickOnViewId(String id, AccessibilityNodeInfo nodeInfo) throws Exception {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (accessibilityNodeInfos == null || accessibilityNodeInfos.size() < 1) {
            Exception e = new Exception("viewId = [" + id + "] not founded!");
            throw e;
        }
        for (int i = 0; i < accessibilityNodeInfos.size(); i++) {
            if (accessibilityNodeInfos.get(i).isEnabled()) {
                accessibilityNodeInfos.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                SystemClock.sleep(500);
            }
        }

    }

    /**
     * Show current UI contents,print by Log.i
     * @param accessibilityNodeInfo
     */
    public static void showCurrentContent(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null)
            return;
        if (accessibilityNodeInfo.getChildCount() == 0) {
            Log.i(TAG, "content:" + accessibilityNodeInfo.getText() + " " + accessibilityNodeInfo.getClassName() + " " + accessibilityNodeInfo.getViewIdResourceName());
        } else {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                showCurrentContent(accessibilityNodeInfo.getChild(i));
            }
        }
    }

    /**
     * Check is there a view with specified id
     * @param id the view id need to be checked,for example "com.android.camera:id/shutter_button"
     * @param nodeInfo AccessibilityNodeInfo
     * @return true if view with this id is exist, else return false
     */
    public static boolean searchId(String id, AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            if (list != null && list.size() > 0)
                return true;
        }
        return false;

    }

    public static void clickOnScreen(int x, int y, AccessibilityNodeInfo nodeInfo) {

        Log.e(TAG, "clickOnScreen: " + x + " " + y);

    }


}
