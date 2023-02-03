package com.polytron.mylauncher.screens;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.polytron.mylauncher.R;
import com.polytron.mylauncher.adapters.AppsControlAdapter;
import com.polytron.mylauncher.adapters.AppsDrawerAdapter;
import com.polytron.mylauncher.models.AppInfo;
import com.polytron.mylauncher.models.AppPreference;

import java.util.List;
import java.util.Objects;

public class Setting extends AppCompatActivity {
    RecyclerView rvAppControl;
    RecyclerView.Adapter appsControlAdapter;
    RecyclerView.LayoutManager layoutManager;
    AppPreference appPreference;
    EditText etPassword;
    ImageView ivSettingBtn;
//    AppsControlAdapter appsControlAdapter;
//    LinearLayoutManager layoutManager;
    private static List<AppInfo> appsList;

    private void showDialog() {
        try{
            final AlertDialog alertDialog;
            final AlertDialog.Builder dialog;
            LayoutInflater inflater;
            final View dialogView;
            TextView title = new TextView(this);
            title.setText("Ganti Password");
            title.setPadding(20, 10, 10, 10);
            title.setTextColor(this.getResources().getColor(R.color.black));
            title.setTextSize(20);
            title.setTypeface(null, Typeface.BOLD);

            inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.custom_alert_dialog_admin, null);

            dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ganti", null)
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
                            if(!Objects.equals(appPreference.getKeyAdminPassword(), password)) {
                                if(appPreference.saveAdminData("", password)) {
                                    Toast.makeText(getApplicationContext(), "Password berhasil diubah!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Password tidak berhasil diubah!", Toast.LENGTH_SHORT).show();
                                }
                                etPassword.setText("");
                                alertDialog.dismiss();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Password Sama!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Password Kosong!", Toast.LENGTH_SHORT).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        appPreference = new AppPreference(this);

        ivSettingBtn = (ImageView) findViewById(R.id.ivSettingBtn);
        rvAppControl = (RecyclerView) findViewById(R.id.rvApplicationManager);
        appsControlAdapter = new AppsControlAdapter(this, this);
        layoutManager = new LinearLayoutManager(this);

        rvAppControl.setLayoutManager(layoutManager);
        rvAppControl.setAdapter(appsControlAdapter);

        ivSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }


}