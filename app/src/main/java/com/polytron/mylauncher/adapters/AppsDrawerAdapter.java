package com.polytron.mylauncher.adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polytron.mylauncher.R;
import com.polytron.mylauncher.models.AppInfo;
import com.polytron.mylauncher.models.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppsDrawerAdapter extends RecyclerView.Adapter<AppsDrawerAdapter.ViewHolder> {
    private static Context context;
    private static List<AppInfo> appsList;
    AppPreference appPreference;
    String appList;
    Activity activity;
    String[] appArray = new String[0];
    int skippedItem = 0;

    public AppsDrawerAdapter(Context c, Activity activity) {
        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon
        context = c;
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        if(appPreference.getKeyAppList() != null && !Objects.equals(appPreference.getKeyAppList(), "")) {
            appList = appPreference.getKeyAppList();
            if(appList.contains("com.google.android.googlequicksearchbox;")) {
                appList = appList.replace("com.google.android.googlequicksearchbox;", "");
                if(!appList.contains("com.google.android.googlequicksearchbox;")) {
                    appList = appList + "com.google.android.googlequicksearchbox;";
                }
            }
            appArray = appList.split(";");
            Log.e(TAG, "appPreference: " + appPreference.getKeyAppList());
        }
        setUpApps(appPreference, appArray, appList);
    }

    public static void setUpApps(AppPreference appPreference, String[] appArray, String appList){
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

            if(appPreference.getKeyAppList() != null) {
                if(appList != null) {
                    appArray = appList.split(";");

                    for (String application:
                            appArray) {
                        if(ri.activityInfo.packageName.equals(application)) {
                            appsList.add(app);
                        }
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_view_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String appLabel = appsList.get(position).getLabel().toString();
        String appPackage = appsList.get(position).getPackageName().toString();
        Drawable appIcon = appsList.get(position).getIcon();

        TextView textView = holder.textView;
        ImageView imageView = holder.img;
        RelativeLayout relativeLayout = holder.rlRowList;
        textView.setText(appLabel);
        imageView.setImageDrawable(appIcon);
        Log.e(TAG, "onBindViewHolder: " + appPackage );

        textView.setTextColor(context.getResources().getColor(R.color.white));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("open app: ", appsList.get(position).getPackageName().toString());
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(position).getPackageName().toString());
                context.startActivity(launchIntent);
//                Log.e("test: ", appsList.get(position).getPackageName().toString());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("open app: ", appsList.get(position).getPackageName().toString());
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(position).getPackageName().toString());
                context.startActivity(launchIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appsList.size() - skippedItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewGroup.LayoutParams params;
        public RelativeLayout rlRowList;
        public TextView textView;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            params = new RelativeLayout.LayoutParams(0,0);
            rlRowList = itemView.findViewById(R.id.rlRowList);
            textView =  itemView.findViewById(R.id.tv_app_name);
            img = itemView.findViewById(R.id.app_icon);

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
