package com.polytron.mylauncher;

import static android.content.ContentValues.TAG;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    final View view = LayoutInflater.inflate(R.layout.fragment_home_screen, , false);
// To keep track of activity's window focus
boolean currentFocus;

// To keep track of activity's foreground/background status
boolean isPaused;

Handler collapseNotificationHandler;

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

//        try{
//            preventStatusBarExpansion(this);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }

        try{
            disablePullNotificationTouch();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
            Log.e("Running Apps: ", runningAppProcessInfo.get(i).processName );
//            if(runningAppProcessInfo.get(i).processName.equals("com.the.app.you.are.looking.for")) {
//                // Do your stuff here.
//            }
        }
//        checkRunningApps();

    }

    String lastAppPN = "com.polytron.mylauncher";
    public void checkRunningApps() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String activityOnTop;
        if (Build.VERSION.SDK_INT > 20) {
            activityOnTop = mActivityManager.getRunningAppProcesses().get(0).processName;
            Log.e(TAG, "checkRunningApps: " + activityOnTop);
        } else {
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            activityOnTop = ar.topActivity.getPackageName();
        }
        //Log.e("activity on TOp", "" + activityOnTop);

        // Provide the packagename(s) of apps here, you want to show password activity
        if (activityOnTop.contains("whatsapp")  // you can make this check even better
                || activityOnTop.contains("com.polytron.mylauncher")) {
            if (!(lastAppPN.equals(activityOnTop))) {
                lastAppPN = activityOnTop;
                Log.e("Whatsapp", "started");
            }
        } else {
            if (lastAppPN.contains("whatsapp")) {
                if (!(activityOnTop.equals(lastAppPN))) {
                    Log.e("Whatsapp", "stoped");
                    lastAppPN = "";
                }
            }
            // DO nothing
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        currentFocus = hasFocus;

        if (!hasFocus) {
//            //basically hide the status bar only
//            View decorView = getWindow().getDecorView();
//            // Hide the status bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);

            // Method that handles loss of window focus
            collapseNow();

        }
    }

    public void collapseNow() {

        // Initialize 'collapseNotificationHandler'
        if (collapseNotificationHandler == null) {
            collapseNotificationHandler = new Handler();
        }

        // If window focus has been lost && activity is not in a paused state
        // Its a valid check because showing of notification panel
        // steals the focus from current activity's window, but does not
        // 'pause' the activity
        if (!currentFocus && !isPaused) {

            // Post a Runnable with some delay - currently set to 300 ms
            collapseNotificationHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    // Use reflection to trigger a method from 'StatusBarManager'

                    Object statusBarService = getSystemService("statusbar");
                    Class<?> statusBarManager = null;

                    try {
                        statusBarManager = Class.forName("android.app.StatusBarManager");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    Method collapseStatusBar = null;

                    try {

                        // Prior to API 17, the method to call is 'collapse()'
                        // API 17 onwards, the method to call is `collapsePanels()`

                        if (Build.VERSION.SDK_INT > 16) {
                            collapseStatusBar = statusBarManager .getMethod("collapsePanels");
                        } else {
                            collapseStatusBar = statusBarManager .getMethod("collapse");
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    collapseStatusBar.setAccessible(true);

                    try {
                        collapseStatusBar.invoke(statusBarService);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    // Check if the window focus has been returned
                    // If it hasn't been returned, post this Runnable again
                    // Currently, the delay is 100 ms. You can change this
                    // value to suit your needs.
                    if (!currentFocus && !isPaused) {
                        collapseNotificationHandler.postDelayed(this, 10L);
                    }

                }
            }, 10L);
        }
    }

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
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
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
        isPaused = false;
        checkPermission();
        try{
            disablePullNotificationTouch();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isPaused = true;
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
    }


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