package com.polytron.mylauncher.screens.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.polytron.mylauncher.R;

public class HomeScreenFragment extends Fragment {
    Button btnNotif;
    ImageView imageViewDrawer;
    public HomeScreenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewDrawer = view.findViewById(R.id.icon_drawer);
        btnNotif = (Button) view.findViewById(R.id.btnNotification);

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //a
//                NotificationManager notif=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                Notification notify=new Notification.Builder
//                        (getApplicationContext()).setContentTitle("title").setContentText("body").
//                        setContentTitle("subject").setSmallIcon(R.drawable.ic_launcher_background).build();
//
//                notify.flags |= Notification.FLAG_AUTO_CANCEL;
//                notif.notify(0, notify);
            }
        });

        imageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AppsDrawerFragment());
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }

        return false;
    }
}