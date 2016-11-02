package script.helper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import script.helper.uiauto.UiAuto;

/**
 * get device info
 */

public class DeviceUtils {

    /**
     * get all app package name on device
     *
     * @return
     */
    public static String[] getAllApps() {
        Context context= UiAuto.context;
        List<String> apps = new ArrayList<>();
        PackageManager pManager = context.getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            apps.add(pak.packageName);
        }
        String[] all_pkgs = (String[]) apps.toArray(new String[apps.size()]);
        return all_pkgs;
    }

}
