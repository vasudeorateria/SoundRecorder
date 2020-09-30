package com.kjstudios.soundrecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.kjstudios.soundrecorder.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        checkPermissions(permissions);

    }

    void checkPermissions(String[] permissions) {

        for (String perm : permissions) {
            if (getApplicationContext().checkSelfPermission(perm) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this, permissions, 200);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String[] rerequest = new String[permissions.length];
        for(int i = 0 ; i< grantResults.length ; i++){
            if(grantResults[i] != 200){
                rerequest[i] = permissions[i];
            }
        }
        if(rerequest.length > 0){
            checkPermissions(rerequest);
        }
    }
}