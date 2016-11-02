## Direction for use

### 1. Copy the `script` package to your project.
 
 the destination dir may be one of follows: 
 - **src** ( for Eclipse IDE) 
 - **src\main\java** (for Andrid Studio IDE) 


### 2. Create MyApplication.java and init UiAuto in your project：

```
    import android.app.Application;
    import android.content.Context;

    
    public class MyApplication extends Application {
        public static Context context;
        public static UiAuto auto;
        @Override
        public void onCreate() {
            super.onCreate();
            context=this;
            auto=new UiAuto(this);
        }
    }
```

### 3. Add name attribute on \<application\> node in AndroidManifest.xml file:

```
<application
        android:name="{$pkg}.MyApplication"
        ...
```

### 4. Add permission on \<manifest\> node in AndroidManifest.xml file:

```
<uses-permission android:name="android.permission.WRITE_SETTINGS" />

```

### 5. Add accessiblity service on \<application\> node in AndroidManifest.xml file:

```
<service
            android:name="script.helper.DetectionService"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>
            
</service>
```

### 6. how to use，you need start you script with a new thread, but not in the main UI thread.

```
new Thread(new Runnable() {
            @Override
            public void run() {
                UiAuto uiAuto=MyApplication.auto;
                uiAuto.pressHome();
                uiAuto.lunchApp("com.android.camera","com.android.camera.CameraLauncher");
                uiAuto.clickOnViewId("com.android.camera:id/shutter_button");
                //... something more

            }
        }).start();

```

#### if there is any question about this project, you can sent you problems to my Email: Crystal1017Chen@gmail.com