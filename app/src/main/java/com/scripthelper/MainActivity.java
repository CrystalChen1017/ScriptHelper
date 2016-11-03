package com.scripthelper;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import script.helper.uiauto.AccessibilityServiceUtil;
import script.helper.uiauto.UiAuto;


public class MainActivity extends AppCompatActivity {
    Button start, turnOnService;
    static String TAG = "MainActivity";
    UiAuto uiAuto=MyApplication.auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.startBtn);
        turnOnService = (Button) findViewById(R.id.turnOnServiceBtn);

    }

    public void turnOnService(View v) {
        Log.i(TAG, "turnOnService:");
        if (!AccessibilityServiceUtil.isAccessibilitySettingsOn(MyApplication.context)) {
            //if service is off, jump to the settings
            startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
        }

    }

    public void startScript(View v) {
        Log.i(TAG, "startScript:");
        if (!AccessibilityServiceUtil.isAccessibilitySettingsOn(MyApplication.context)) {
            Toast.makeText(this, "Service is not on!", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                uiAuto.pressHome();
//                uiAuto.lunchApp("com.mlab.cam","com.android.camera.CameraLauncher");
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.mlab.cam","com.android.camera.CameraLauncher");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
                uiAuto.clickOnText("下一步");
                uiAuto.clickOnText("下一步");
                uiAuto.clickOnText("完成");
                uiAuto.clickOnText("备忘录");
                uiAuto.clickOnText("备忘录");
                uiAuto.clickOnText("添加");
                uiAuto.enterTextByViewId("com.meitu.mobile.notes:id/note_container","123456");
                //... something more

            }
        }).start();
    }
}
