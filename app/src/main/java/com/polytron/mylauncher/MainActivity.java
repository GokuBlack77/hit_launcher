package com.polytron.mylauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.polytron.mylauncher.models.CustomViewGroup;
import com.polytron.mylauncher.screens.fragments.HomeScreenFragment;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
//    final View view = LayoutInflater.inflate(R.layout.fragment_home_screen, , false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeScreenFragment());
//        NotificationManagerCompat.from(this).cancelAll();

//        if(Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("package:");
//            sb.append(getPackageName());
//            startActivityForResult(new
//                    Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION",
//                    Uri.parse(sb.toString())),123);
//        }


        checkPermission();

        try{
            preventStatusBarExpansion(this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

//        try{
//            disablePullNotificationTouch();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }
//
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 2277);
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

//    public void addOverlay() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                askedForOverlayPermission = true;
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
//            }
//        }
//    }

    private void disablePullNotificationTouch() {
        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (25 * getResources()
                .getDisplayMetrics().scaledDensity);
//        localLayoutParams.format = PixelFormat.RGBX_8888;
        CustomViewGroup view = new CustomViewGroup(this);
        manager.addView(view, localLayoutParams);
    }

    public void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity)context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        CustomViewGroup  view = new CustomViewGroup(context);

        manager.addView(view, localLayoutParams);
    }

//    public static void disableStatusBar(Context context) {
//        Log.d(MainActivity.class.getCanonicalName(), "disableStatusBar: ");
//        // Read from property or pass it in function, whatever works for you!
//
//        boolean disable = SystemProperties.getBoolean(context, "supercool.status.bar.disable", true);
//        Object statusBarService = context.getSystemService("statusbar");
//
//        Class<?> statusBarManager = null;
//        try {
//            statusBarManager = Class.forName("android.app.StatusBarManager");
//            try {
//                final Method disable_statusBarFeatures = statusBarManager.getMethod("disable", int.class);
//                try {
//                    disable_statusBarFeatures.setAccessible(true);
//                    if (disable) {
//                        disable_statusBarFeatures.invoke(statusBarService, 0x00010000 | 0x00040000);
//                    } else {
//                        disable_statusBarFeatures.invoke(statusBarService, 0x00000000);
//                    }
//                } catch (Exception e) {
//                    Log.e(MainActivity.class.getCanonicalName(), "disableStatusBar: " + e.getMessage(), e);
//                }
//
//            } catch (Exception e) {
//                Log.e(Home.class.getCanonicalName(), "disableStatusBar: " + e.getMessage(), e);
//
//            }
//        } catch (Exception e) {
//            Log.e(Home.class.getCanonicalName(), "disableStatusBar: " + e.getMessage(), e);
//
//        }
//    }

    protected void onResume() {
        super.onResume();
        checkPermission();
        try{
            disablePullNotificationTouch();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
//        //basically hide the status bar only
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        loadFragment(new HomeScreenFragment());
    }


}