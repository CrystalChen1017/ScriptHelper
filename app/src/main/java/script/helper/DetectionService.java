package script.helper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import script.helper.uiauto.AccessibilityServiceUtil;
import script.helper.utils.DeviceUtils;


public class DetectionService extends AccessibilityService {
    private static final String TAG = "DetectionService";
    String[] PACKAGES = DeviceUtils.getAllApps();
    private static AccessibilityNodeInfo lastList;
    public static AccessibilityService myAccService;


    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "----------DetectionService is connected!");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        //the package list your care about
        accessibilityServiceInfo.packageNames = PACKAGES;
        //important: Only after set this flag, can you get view id informations
        accessibilityServiceInfo.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        //the event type you care about
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        //if set this spoken, you would get nothing about view informations
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        accessibilityServiceInfo.notificationTimeout = 1000;
        setServiceInfo(accessibilityServiceInfo);
        myAccService = this;
    }



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i("DetectionService", "DetectionService_onAccessibilityEvent: " + event.toString());

        lastList = getRootInActiveWindow();
        AccessibilityServiceUtil.showCurrentContent(lastList);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "----------DetectionService is interrupted!");
    }

    public static AccessibilityNodeInfo getLastList() {
        return lastList;
    }

}