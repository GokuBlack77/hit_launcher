package com.polytron.mylauncher.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polytron.mylauncher.R;
import com.polytron.mylauncher.models.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppsDrawerAdapter extends RecyclerView.Adapter<AppsDrawerAdapter.ViewHolder> {
    private static Context context;
    private static List<AppInfo> appsList;

    public AppsDrawerAdapter(Context c ) {
        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon
        context = c;
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
                    ri.activityInfo.loadIcon(pManager)
            );
            Log.i(" Log package ", app.getPackageName().toString());
            appsList.add(app);
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
        textView.setText(appLabel);
        ImageView imageView = holder.img;
        imageView.setImageDrawable(appIcon);

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
        return appsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
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
