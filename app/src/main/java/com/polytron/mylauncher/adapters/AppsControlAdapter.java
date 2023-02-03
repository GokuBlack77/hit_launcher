package com.polytron.mylauncher.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polytron.mylauncher.R;
import com.polytron.mylauncher.models.AppInfo;
import com.polytron.mylauncher.models.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppsControlAdapter extends RecyclerView.Adapter<AppsControlAdapter.ViewHolder> {
    private static Context context;
    private static List<AppInfo> appsList;
    AppPreference appPreference;
    StringBuilder appListBuilder = new StringBuilder();
    String appList;
    Activity activity;
    String[] appArray = new String[0];

    public AppsControlAdapter(Context c, Activity activity) {
        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon
        context = c;
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        appList = appPreference.getKeyAppList();
        if(appList != null && !appList.equals("")) {
            if(!appListBuilder.toString().contains(appList)) {
                appListBuilder.append(appList);
            }
        }
        setUpApps();
    }

    public static void setUpApps(){
        PackageManager pManager = context.getPackageManager();
        appsList = new ArrayList<AppInfo>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pManager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo(
                    ri.loadLabel(pManager),
                    ri.activityInfo.packageName,
                    ri.activityInfo.loadIcon(pManager),
                    false
            );
            Log.i(" Log package ", app.getPackageName().toString());
            appsList.add(app);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_row_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        appPreference = new AppPreference(this.activity);

        String appLabel = appsList.get(position).getLabel().toString();
        String appPackage = appsList.get(position).getPackageName().toString();
        Drawable appIcon = appsList.get(position).getIcon();

        final AppInfo appInfo = appsList.get(position);

        CheckBox checkBox = holder.checkBox;
        checkBox.setText(appLabel);
        ImageView imageView = holder.img;
        imageView.setImageDrawable(appIcon);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isPressed()){
                    if(isChecked) {
                        appInfo.setSelected(true);
                        appListBuilder.append(appPackage + ";");
                        appList = appListBuilder.toString();
//                        appPreference.saveAppList(appList);

                    }
                    else {
                        appInfo.setSelected(false);
                        if(appList.contains(appPackage + ";")) {
                            Log.e(TAG, "Same package found");
                            appList = appList.replace(appPackage + ";", "");
                            appListBuilder = appListBuilder.replace(appListBuilder.indexOf(appPackage + ";"), appListBuilder.indexOf(appPackage + ";")+appPackage.length()+1, "");
//                            if(appListBuilder.indexOf(appPackage + ";") != -1) {
//                            }
                        }
                    }

                    Log.e(TAG, appList);
                    if(appPreference.saveAppList(appList)) {
                        Log.i(TAG, "Save App Preference Success!");
                    }
                    else {
                        Log.i(TAG, "Save App Preference Error!");
                    }

                    Log.e(TAG, "Preferences: " + appPreference.getKeyAppList() );
                }
            }});
        checkBox.setChecked(appInfo.isSelected());

        if(!Objects.equals(appPreference.getKeyAppList(), "") && appPreference.getKeyAppList() != null) {
            appList = appPreference.getKeyAppList();
            if(appList != null) {
                appArray = appList.split(";");
                for (String app:
                        appArray) {
                    if(appPackage.contains(app)) {
                        checkBox.setChecked(true);
                    }
                }
            }

//            Log.e(TAG, "appList: " + appList);
//            Log.e(TAG, "appListBuilder: " + appListBuilder.toString());
//            appPreference.clearAppListData();
        }
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("open app: ", appsList.get(position).getPackageName().toString());
//                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(position).getPackageName().toString());
//                context.startActivity(launchIntent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CheckBox checkBox;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            checkBox =  itemView.findViewById(R.id.cbAppControl);
            img = itemView.findViewById(R.id.ivAppIcon);

        }

        @Override
        public void onClick(View view) {
//            int pos = getAdapterPosition();
//
//            Context context = view.getContext();
//
//            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).getPackageName().toString());
//            context.startActivity(launchIntent);
//            Log.e("applist: ", appsList.get(pos).getPackageName().toString());
        }
    }
}
