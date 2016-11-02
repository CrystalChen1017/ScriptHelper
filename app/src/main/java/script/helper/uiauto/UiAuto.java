package script.helper.uiauto;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import script.helper.DetectionService;


/**
 * Using UiAuto to simplifiy your scripts' operations
 */

public class UiAuto {
    private final String TAG = "UiAuto";
    public static Context context;

    public UiAuto(Context context) {
        this.context = context;
    }

    private void before() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void after() {

    }

    /**
     * Lunch specified app
     *
     * @param pkg     app package name,for example"com.android.camera"
     * @param actName app activity name,for example "com.android.camera.CameraLauncher"
     */
    public void lunchApp(String pkg, String actName) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(pkg, actName);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * Click on the view with specified text
     * <p><b>Note: </b>if there are many views satisfied this text, all views will be clicked!</p>
     */
    public void clickOnText(String text) {
        before();
        AccessibilityNodeInfo list = DetectionService.getLastList();
        if (list == null) {
            Log.e(TAG, "clickOnText: current view list is null!");
            return;
        }
        try {
            AccessibilityServiceUtil.clickOnText(text, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();
    }

    /**
     * Click on the view with specified id
     * <p><b>Note: </b>if there are many views satisfied this id, all views will be clicked!</p>
     */
    public void clickOnViewId(String id) {
        before();
        AccessibilityNodeInfo list = DetectionService.getLastList();
        if (list == null) {
            Log.e(TAG, "clickOnViewId: current view list is null!");
            return;
        }
        try {
            AccessibilityServiceUtil.clickOnViewId(id, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();
    }

    /**
     * Check if text is exist
     *
     * @return true if text is exist, else return false
     */
    public boolean searchText(String text) {
        before();
        AccessibilityNodeInfo list = DetectionService.getLastList();
        if (list == null) {
            Log.e(TAG, "searchText: current view list is null!");
            return false;
        }
        return AccessibilityServiceUtil.searchText(text, list);
    }

    /**
     * Check is there a view with specified id
     *
     * @return true if view with this id is exist, else return false
     */
    public boolean searchId(String id) {
        before();
        AccessibilityNodeInfo list = DetectionService.getLastList();
        if (list == null) {
            Log.e(TAG, "searchId: current view list is null!");
            return false;
        }
        return AccessibilityServiceUtil.searchId(id, list);
    }

    /**
     * Press HOME
     */
    public void pressHome() {
        before();
        if (DetectionService.myAccService == null) {
            Log.e(TAG, "pressHome: AccessibilityService is off, please turn it on!！");
            return;
        }
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        after();
    }
    /**
     * Press BACK
     */
    public void pressBack() {
        before();
        if (DetectionService.myAccService == null) {
            Log.e(TAG, "pressHome: AccessibilityService is off, please turn it on!！");
            return;
        }
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        after();
    }

    /**
     * Show quick settings
     */
    public void showQuickSettings() {
        before();
        if (DetectionService.myAccService == null) {
            Log.e(TAG, "pressHome: AccessibilityService is off, please turn it on!！");
            return;
        }
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS);
        after();
    }

    /**
     * Show notifications
     */
    public void showNotifications() {
        before();
        if (DetectionService.myAccService == null) {
            Log.e(TAG, "pressHome: AccessibilityService is off, please turn it on!！");
            return;
        }
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
        after();
    }
    /**
     * Show recents task
     */
    public void showRecentsTask() {
        before();
        if (DetectionService.myAccService == null) {
            Log.e(TAG, "pressHome: AccessibilityService is off, please turn it on!！");
            return;
        }
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        after();
    }

    /**
     * Press POWER
     */
    public void pressPower() {
        before();
        DetectionService.myAccService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
        after();
    }

    public void clickOnScreen(int x, int y) {
        before();
        AccessibilityNodeInfo list = DetectionService.getLastList();
        if (list == null) {
            Log.e(TAG, "clickOnViewId: current view list is null!");
            return;
        }
        try {
            AccessibilityServiceUtil.clickOnScreen(x, y, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();

    }
}
