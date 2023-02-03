package com.polytron.mylauncher.screens.fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.polytron.mylauncher.MainActivity;
import com.polytron.mylauncher.R;
import com.polytron.mylauncher.adapters.AppsDrawerAdapter;
import com.polytron.mylauncher.models.AppPreference;
import com.polytron.mylauncher.screens.Setting;

import java.util.Objects;
import java.util.Set;

public class HomeScreenFragment extends Fragment {
    Button btnNotif, btnSetting;
    ImageView imageViewDrawer, ivSettingBtn;
    EditText etPassword;
    RecyclerView recyclerView;
    AppsDrawerAdapter adapter;
    GridLayoutManager gridLayoutManager;
    AppPreference appPreference;
    public HomeScreenFragment() {
    }

    private void showDialog(View view) {
        try{
            final AlertDialog alertDialog;
            final AlertDialog.Builder dialog;
            LayoutInflater inflater;
            final View dialogView;
            TextView title = new TextView(getContext());
            title.setText("Masukan Password");
            title.setPadding(20, 10, 10, 10);
            title.setTextColor(getActivity().getResources().getColor(R.color.black));
            title.setTextSize(20);
            title.setTypeface(null, Typeface.BOLD);

            inflater = getActivity().getLayoutInflater();
            dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

            dialog = new AlertDialog.Builder(getActivity())
                    .setView(dialogView)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Login", null)
                    .setCustomTitle(title);

            alertDialog = dialog.show();

            Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "ur password: " + appPreference.getKeyAdminPassword() );
                    etPassword = dialogView.findViewById(R.id.etPassword);
                    if(etPassword != null) {
                        String password = etPassword.getText().toString().trim();
                        if(!password.equals("")) {
                            if(Objects.equals(appPreference.getKeyAdminPassword(), password)) {
                                etPassword.setText("");
                                alertDialog.dismiss();
                                Intent intent = new Intent(getContext(), Setting.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getContext(), "Password Salah!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Password Kosong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onResume() {
        super.onResume();
        appPreference = new AppPreference(getActivity());
        adapter = new AppsDrawerAdapter(getContext(), getActivity());
        gridLayoutManager = new GridLayoutManager(getContext(),4);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreference = new AppPreference(getActivity());
        imageViewDrawer = view.findViewById(R.id.icon_drawer);
        btnNotif = (Button) view.findViewById(R.id.btnNotification);
//        btnSetting = (Button) view.findViewById(R.id.btnSetting);
        recyclerView = (RecyclerView) view.findViewById(R.id.appDrawer_recylerView);
        ivSettingBtn = (ImageView) view.findViewById(R.id.ivSettingBtn);

        adapter = new AppsDrawerAdapter(getContext(), getActivity());

//        layoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(),4);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService
//        }

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ab
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "hello");
                builder.setContentTitle("Title here");
                builder.setContentText("Hello there");
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(HomeScreenFragment.this.getContext());
                managerCompat.notify(1, builder.build());
            }
        });

//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), Setting.class);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });



        ivSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
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