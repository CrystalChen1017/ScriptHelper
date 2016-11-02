package com.scripthelper;

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
                uiAuto.pressPower();
//                uiAuto.lunchApp("com.android.camera","com.android.camera.CameraLauncher");
//                uiAuto.clickOnViewId("com.android.camera:id/shutter_button");
                //... something more

            }
        }).start();
    }
}
